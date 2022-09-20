package com.atguigu.easyexcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.atguigui.easyexcel.dto.ExcelStudentDTO;
import com.atguigui.easyexcel.listener.ExcelStudentDTOListener;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * projectName: srb
 *
 * @author: GOD伟
 * time: 2022/6/16 14:32 周四
 * description:
 */
public class ExcelWriteXlsx {
    @Test
    public void simpleWriteXlsx() {
        // String fileName = "d:/excel/simpleWrite.xlsx"; //需要提前新建目录
        //  EasyExcel.write(fileName, ExcelStudentDTO.class).sheet("模板1").doWrite(data());
        ArrayList<ExcelStudentDTO> list = new ArrayList<>();
        /*算上标题*/
        for (int i = 0; i < 2000; i++) {
            ExcelStudentDTO data = new ExcelStudentDTO();
            data.setName("IRobot" + i + "号");
            data.setBirthday(new Date());
            data.setSalary(3000d + i);
            list.add(data);
        }
        ExcelWriter excelWriter = EasyExcel.write(new File("d:/excel/studentBatch.xlsx"), ExcelStudentDTO.class).build();
        WriteSheet sheet1 = EasyExcel.writerSheet("sheet1").sheetNo(1).build();
        WriteSheet sheet2 = EasyExcel.writerSheet("sheet2").sheetNo(2).build();
        excelWriter.write(list, sheet1);
        excelWriter.write(new ArrayList<Object>(), sheet2);
        excelWriter.finish();

    }

    @Test
    public void simpleReadXlsx() {
        String fileName = "d:/excel/simpleWrite.xlsx";
        //这里默认读取第一个sheet
        //EasyExcel.read(fileName, ExcelStudentDTO.class, new ExcelStudentDTOListener()).sheet().doRead();
        EasyExcel.read(new File("d:/excel/studentBatch.xlsx"), ExcelStudentDTO.class, new ExcelStudentDTOListener()).sheet().doRead();
    }

    //辅助方法
    private List<ExcelStudentDTO> data() {
        ArrayList<ExcelStudentDTO> list = new ArrayList<>();
        /*算上标题*/
        for (int i = 0; i < 65535; i++) {
            ExcelStudentDTO data = new ExcelStudentDTO();
            data.setName("IRobot" + i + "号");
            data.setBirthday(new Date());
            data.setSalary(3000d + i);
            list.add(data);
        }
        return list;
    }
}
