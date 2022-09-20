package com.atguigu.srb.sms.service;

import com.atguigu.srb.base.dto.SmsDTO;

/**
 * projectName: srb
 *
 * @author: GOD伟
 * time: 2022/6/19 17:49 周日
 * description:
 */
public interface SmsService {

     void send(String mobile) ;

     void sendRecharge(SmsDTO smsDTO);
}
