package com.atguigu.srb.core.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.srb.core.mapper.DictMapper;
import com.atguigu.srb.core.pojo.dto.ExcelDictDTO;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * projectName: srb
 *
 * @author: GOD伟
 * time: 2022/6/17 15:34 周五
 * description:
 */

@NoArgsConstructor
public class ExcelDictDTOListener extends AnalysisEventListener<ExcelDictDTO> {
    List<ExcelDictDTO> excelDictDTOList = new ArrayList<>();
    DictMapper dictMapper;

    //有参构造
    public ExcelDictDTOListener(DictMapper dictMapper) {
        this.dictMapper = dictMapper;
    }

    @Override
    public void invoke(ExcelDictDTO excelDictDTO, AnalysisContext analysisContext) {

        excelDictDTOList.add(excelDictDTO);
        if (excelDictDTOList.size() >= 15) {
            System.out.println("每监听15次,处理一次数据" + excelDictDTOList.size());
            dictMapper.insertBatch(excelDictDTOList);
            excelDictDTOList.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

        System.out.println("收尾" + excelDictDTOList.size());

        /*收尾工作 加个判断*/
        if (excelDictDTOList.size() > 0) {
            dictMapper.insertBatch(excelDictDTOList);
        }

    }
}
