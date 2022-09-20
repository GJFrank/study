package com.atguigu.srb.core.controller.admin;


import com.alibaba.excel.EasyExcel;
import com.atguigu.srb.common.result.R;
import com.atguigu.srb.core.pojo.dto.ExcelDictDTO;
import com.atguigu.srb.core.pojo.entity.Dict;
import com.atguigu.srb.core.service.DictService;
import io.swagger.annotations.Api;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 数据字典 前端控制器
 * </p>
 *
 * @author God伟
 * @since 2022-06-12
 */
//@CrossOrigin
@RestController
@RequestMapping("/admin/core/dict")
@Api(tags = "数据字典管理接口")
@Slf4j
public class AdminDictController {

    @Autowired
    DictService dictService;

    @PostMapping("import")
    public void importDict(@RequestParam("file")MultipartFile multipartFile){
        //调用easyexcel导入数据
        dictService.importDict(multipartFile);
        System.out.println(1);
    }

    @GetMapping("getDictListByParentId/{parentId}")
    public R getDictListByParentId(@PathVariable("parentId") Long parentId){
       List<Dict> dicts= dictService.getDictListByParentId(parentId);
       return R.ok().data("list",dicts);
    }

    @SneakyThrows
    @GetMapping("export")
    public void export(HttpServletResponse response){
        //先获取数据
       List<ExcelDictDTO> excelDictDTOS=dictService.getExcelDictDTOList();

       response.setCharacterEncoding("utf-8");
       response.setContentType("application/vnd.excel"); //输出为excel格式文件
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + "dicts" + ".xlsx");

        EasyExcel.write(response.getOutputStream(),ExcelDictDTO.class).sheet("dict").doWrite(excelDictDTOS);
    }

}

