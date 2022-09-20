package com.atguigu.srb.oss.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * projectName: srb
 *
 * @author: GOD伟
 * time: 2022/6/20 12:16 周一
 * description:
 */
public interface FileService {
    /**
     * 文件上传至阿里云
     */
    String upload(String module, MultipartFile multipartFile);

    void removeFile(String url);
}

