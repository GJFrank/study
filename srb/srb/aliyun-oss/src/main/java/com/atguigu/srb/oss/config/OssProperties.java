package com.atguigu.srb.oss.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * projectName: srb
 *
 * @author: GOD伟
 * time: 2022/6/20 12:09 周一
 * description: 从配置文件读取常量
 */
@Data
@Component
@ConfigurationProperties(prefix = "aliyun.oss")
public class OssProperties implements InitializingBean {
    /*
    aliyun.oss.endpoint = "https://oss-cn-shenzhen.aliyuncs.com";
    aliyun.oss.accessKeyId = "LTAI5tDQu3LdCmCELSTu6rMq";
    aliyun.oss.accessKeySecret = "cIQm3jRgVVluyFEvrq9KccmkGlyeFF";
    aliyun.oss.bucketName = "srb-file-jqw-01";*/

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

    public static String ENDPOINT;
    public static String KEY_ID;
    public static String KEY_SECRET;
    public static String BUCKET_NAME;


    @Override
    public void afterPropertiesSet() throws Exception {
        ENDPOINT = endpoint;
        KEY_ID = accessKeyId;
        KEY_SECRET = accessKeySecret;
        BUCKET_NAME = bucketName;
    }
}
