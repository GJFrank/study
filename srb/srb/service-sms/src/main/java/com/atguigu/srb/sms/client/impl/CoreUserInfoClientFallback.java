package com.atguigu.srb.sms.client.impl;

import com.atguigu.srb.sms.client.CoreUserInfoClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * projectName: srb
 *
 * @author: GOD伟
 * time: 2022/6/27 20:56 周一
 * description:服务降级实现类
 */
@Service
@Slf4j
public class CoreUserInfoClientFallback implements CoreUserInfoClient {
    @Override
    public boolean checkMobile(String mobile) {
        System.out.println("服务出现问题, 默认手机号已经被注册!");
        return false;  //true为可用,不存在
    }
}
