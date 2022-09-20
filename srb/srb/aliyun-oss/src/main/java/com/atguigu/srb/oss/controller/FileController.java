package com.atguigu.srb.oss.controller;

import com.atguigu.srb.common.exception.BusinessException;
import com.atguigu.srb.common.result.R;
import com.atguigu.srb.common.result.ResponseEnum;
import com.atguigu.srb.oss.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;

/**
 * projectName: srb
 *
 * @author: GOD伟
 * time: 2022/6/20 13:19 周一
 * description:
 */
@Api(tags = "阿里云文件管理")
//@CrossOrigin //跨域
@RestController
@RequestMapping("api/oss/file")
public class FileController {
    @Autowired
     FileService fileService;

    //module 文件类型  multipartFile  spring管理的文件
    @ApiOperation("文件上传")
    @PostMapping("upload")
    public R upload(@RequestParam("module") String module ,@RequestParam("file")MultipartFile multipartFile){
        String url = fileService.upload(module, multipartFile);
        return R.ok().data("url",url);
    }

    //删除接口
    @ApiOperation("删除文件接口")
    @DeleteMapping("remove")
    public R remove(@RequestParam("url")String url){
        fileService.removeFile(url);
        return R.ok();
    }


}
