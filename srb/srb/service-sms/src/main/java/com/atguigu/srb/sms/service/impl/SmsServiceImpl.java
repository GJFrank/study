package com.atguigu.srb.sms.service.impl;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.atguigu.srb.base.dto.SmsDTO;
import com.atguigu.srb.common.utils.RandomUtils;
import com.atguigu.srb.sms.config.SmsProperties;
import com.atguigu.srb.sms.service.SmsService;
import com.google.gson.Gson;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * projectName: srb
 *
 * @author: GOD伟
 * time: 2022/6/19 18:08 周日
 * description:
 */
@Service
@PropertySource(value = "classpath:application.properties",encoding = "utf-8")
public class SmsServiceImpl implements SmsService {
    @Autowired
    RedisTemplate redisTemplate;


    @Override
    public void send(String mobile) {
        //测试调用短信api
        DefaultProfile profile = DefaultProfile.getProfile(SmsProperties.REGION_ID, SmsProperties.KEY_ID, SmsProperties.KEY_SECRET);
        IAcsClient iAcsClient = new DefaultAcsClient(profile);

        //请求接口参数
        CommonRequest commonRequest = new CommonRequest();
        commonRequest.setSysMethod(MethodType.POST);
        commonRequest.setSysDomain("dysmsapi.aliyuncs.com");
        commonRequest.setSysVersion("2017-05-25");// 接口版本
        commonRequest.setSysAction("SendSms");// 接口名
        commonRequest.putQueryParameter("PhoneNumbers", mobile);

        //解决properties中文乱码  Properties文件用的Encoding是 ISO8859-1编码的
        String signName = null;
        try {
            signName = new String(SmsProperties.SIGN_NAME.getBytes("ISO8859-1"),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //String signName="北京课时教育";
        commonRequest.putQueryParameter("SignName", signName);
        commonRequest.putQueryParameter("TemplateCode", SmsProperties.TEMPLATE_CODE);

        // 也使用json工具制作参数
        Map<String, String> map = new HashMap<>();
        String sixBitRandom = RandomUtils.getSixBitRandom();

        // 将验证码存入缓存
        redisTemplate.opsForValue().set("srb:sms:code:" + mobile, sixBitRandom);
        map.put("code", sixBitRandom);
        String json = JSON.toJSONString(map);
        commonRequest.putQueryParameter("TemplateParam", json);

        // 发送
        CommonResponse commonResponse = null;
        try {
            commonResponse = iAcsClient.getCommonResponse(commonRequest);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        System.out.println(commonResponse);
    }

    @Override
    public void sendRecharge(SmsDTO smsDTO) {
        System.out.println("给手机号"+smsDTO.getMobile()+"发送信息"+smsDTO.getMessage());
    }
}

