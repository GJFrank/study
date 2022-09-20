package com.atguigui.easyexcel.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * projectName: srb
 *
 * @author: GOD伟
 * time: 2022/6/16 14:29 周四
 * description:
 */
@Data
public class ExcelStudentDTO {

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("生日")
    private Date birthday;

    @ExcelProperty("薪资")
    private Double salary;
}
