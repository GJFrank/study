package com.atguigu.srb.core.service;

import com.atguigu.srb.core.pojo.dto.ExcelDictDTO;
import com.atguigu.srb.core.pojo.entity.Dict;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 数据字典 服务类
 * </p>
 *
 * @author God伟
 * @since 2022-06-12
 */
public interface DictService extends IService<Dict> {

    public void importDict(MultipartFile multipartFile);

    List<Dict> getDictListByParentId(Long parentId);

    List<ExcelDictDTO> getExcelDictDTOList();

    List<Dict> getDictListByDictCode(String dictCode);

    String getNameByParentDictCodeAndValue(String dictCode, Integer value);
}
