package com.atguigu.srb.core.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.srb.core.listener.ExcelDictDTOListener;
import com.atguigu.srb.core.pojo.dto.ExcelDictDTO;
import com.atguigu.srb.core.pojo.entity.Dict;
import com.atguigu.srb.core.mapper.DictMapper;
import com.atguigu.srb.core.service.DictService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 数据字典 服务实现类
 * </p>
 *
 * @author God伟
 * @since 2022-06-12
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public void importDict(MultipartFile multipartFile) {
        //使用easyexcel导入mysql 数据库

        try {
            EasyExcel.read(multipartFile.getInputStream(), ExcelDictDTO.class, new ExcelDictDTOListener(baseMapper)).sheet("数据字典").doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Dict> getDictListByParentId(Long parentId) {
        List<Dict> dicts = new ArrayList<>();

        //查询缓存
        dicts = (List<Dict>) redisTemplate.opsForValue().get("srb:core:dictList:" + parentId);
        if (null == dicts || dicts.size() == 0) {
            // 查询数据库
            dicts = getDictListByParentIdFromDb(parentId);
            if (null != dicts && dicts.size() > 0) {
                // 将数据库结果同步给缓存
                redisTemplate.opsForValue().set("srb:core:dictList:" + parentId, dicts);
            } else {
                // 如果key不存在，放入一个空数据，10秒过期
                redisTemplate.opsForValue().set("srb:core:dictList:" + parentId, new ArrayList<Dict>(), 10, TimeUnit.SECONDS);
            }
        }
        return dicts;
    }

    private List<Dict> getDictListByParentIdFromDb(Long parentId) {
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.eq("parent_id", parentId);
        List<Dict> dicts = baseMapper.selectList(dictQueryWrapper);

        for (Dict dict : dicts) {
            Long id = dict.getId();
            QueryWrapper<Dict> dictQueryWrapperChildren = new QueryWrapper<>();
            dictQueryWrapperChildren.eq("parent_id", id);
            Integer integer = baseMapper.selectCount(dictQueryWrapperChildren);
            if (integer > 0) {
                dict.setHasChildren(true);
            }
        }
        return dicts;
    }

    @Override
    public List<ExcelDictDTO> getExcelDictDTOList() {
        List<Dict> dicts = baseMapper.selectList(null);
        List<ExcelDictDTO> excelDictDTOS = dicts.stream().map(dict -> {
            ExcelDictDTO excelDictDTO = new ExcelDictDTO();
            BeanUtils.copyProperties(dict, excelDictDTO);
            return excelDictDTO;
        }).collect(Collectors.toList());
        return excelDictDTOS;
    }

    @Override
    public List<Dict> getDictListByDictCode(String dictCode) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dict_code", dictCode);
        Dict dict = baseMapper.selectOne(queryWrapper);

        QueryWrapper<Dict> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("parent_id", dict.getId());
        List<Dict> dicts = baseMapper.selectList(queryWrapper1);
        return dicts;
    }

    @Override
    public String getNameByParentDictCodeAndValue(String dictCode, Integer value) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dict_code", dictCode);
        Dict dict = baseMapper.selectOne(queryWrapper);

        QueryWrapper<Dict> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("parent_id", dict.getId())
                .eq("value", value);
        Dict dict1 = baseMapper.selectOne(queryWrapper1);
        String name = dict1.getName();
        return name;
    }
}
