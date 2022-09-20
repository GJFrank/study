package com.atguigu.srb.sms.config;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * projectName: srb
 *
 * @author: GOD伟
 * time: 2022/6/19 17:05 周日
 * description:配置常量封装公共参数
 */
@Data
@Component
@ConfigurationProperties(prefix = "aliyun.sms")
public class SmsProperties implements InitializingBean {

    public static String REGION_ID;
    public static String KEY_ID;
    public static String KEY_SECRET;
    public static String TEMPLATE_CODE;
    public static String SIGN_NAME;

    private String regionId;
    private String keyId;
    private String keySecret;
    private String templateCode;
    private String signName;

    //在bean被实例化(在内存中创建实例),后执行afterPropertiesSet()方法
    @Override
    public void afterPropertiesSet() throws Exception {
        REGION_ID = regionId;
        KEY_ID = keyId;
        KEY_SECRET = keySecret;
        TEMPLATE_CODE = templateCode;
        SIGN_NAME = signName;

    }
}
