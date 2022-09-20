package com.atguigu.srb.core.mapper;

import com.atguigu.srb.core.pojo.dto.ExcelDictDTO;
import com.atguigu.srb.core.pojo.entity.Dict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 数据字典 Mapper 接口
 * </p>
 *
 * @author God伟
 * @since 2022-06-12
 */
public interface DictMapper extends BaseMapper<Dict> {

    public void insertBatch(@Param("excelDictDTOList") List<ExcelDictDTO> excelDictDTOList);
}
