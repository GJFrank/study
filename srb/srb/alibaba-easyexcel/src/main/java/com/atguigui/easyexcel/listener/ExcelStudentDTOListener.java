package com.atguigui.easyexcel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigui.easyexcel.dto.ExcelStudentDTO;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * projectName: srb
 *
 * @author: GOD伟
 * time: 2022/6/16 15:03 周四
 * description:
 */
@Slf4j
public class ExcelStudentDTOListener extends AnalysisEventListener<ExcelStudentDTO> {

    /*微批次处理模式*/
    List<ExcelStudentDTO> list = new ArrayList<>();

    /*每一条数据解析都会来调用*/
    @Override
    public void invoke(ExcelStudentDTO data, AnalysisContext context) {
        // log.info("解析到一条数据:{}", data, new Date());
        list.add(data);
        if (list.size() >= 17) {
            System.out.println("每读取17条数据,执行一次" + list.size());
            list.clear();
        }
    }

    /*所有的数据解析完成了, 都会来调用*/
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // log.info("所有数据解析完成!");
        System.out.println("收尾工作" + list.size());

        /*收尾工作 加个判断*/
        if (list.size() > 0) {
            //dictMapper.insertBatch(excelDictDTOS);
        }
    }
}
