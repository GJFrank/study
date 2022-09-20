package com.atguigu.srb.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.atguigu.srb.oss.config.OssProperties;
import com.atguigu.srb.oss.service.FileService;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * projectName: srb
 *
 * @author: GOD伟
 * time: 2022/6/20 12:18 周一
 * description:
 */
@Service
public class FileServiceImpl implements FileService {

    @Override
    public String upload(String module, MultipartFile multipartFile) {
        //  System.out.println(OssProperties.BUCKET_NAME);
        OSS ossClient = new OSSClientBuilder().build(OssProperties.ENDPOINT, OssProperties.KEY_ID, OssProperties.KEY_SECRET);
        //    System.out.println(OssProperties.BUCKET_NAME);
        String fileType = module;
        String dataString = new DateTime().toString("/yyyy/MM/dd/");
        String originalFilename = multipartFile.getOriginalFilename();
        String fileName = UUID.randomUUID().toString();
        fileName = fileName.replace("-", "");
        String filenameExtension = StringUtils.getFilenameExtension(originalFilename);

        String holeName = module + dataString + fileName + "." + filenameExtension;

        boolean b = ossClient.doesBucketExist(OssProperties.BUCKET_NAME);
        if (!b) {
            ossClient.createBucket(OssProperties.BUCKET_NAME);
            ossClient.setBucketAcl(OssProperties.BUCKET_NAME, CannedAccessControlList.PublicRead);
        }
        try {
            ossClient.putObject(OssProperties.BUCKET_NAME, holeName, multipartFile.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        String url = "https://" + OssProperties.BUCKET_NAME + "." + OssProperties.ENDPOINT + "/" + holeName;
        ossClient.shutdown();
        return url;

    }

    @Override
    public void removeFile(String url) {
        OSS ossClient = new OSSClientBuilder().build(OssProperties.ENDPOINT, OssProperties.KEY_ID, OssProperties.KEY_SECRET);
        Integer index = ("https://" + OssProperties.BUCKET_NAME + "." + OssProperties.ENDPOINT + "/").length();
        String fileName = url.substring(index);
        ossClient.deleteObject(OssProperties.BUCKET_NAME,fileName);
        ossClient.shutdown();
    }
}

