package com.atguigu.srb.sms.controller.api;

import com.atguigu.srb.common.exception.Assert;
import com.atguigu.srb.common.result.R;
import com.atguigu.srb.common.result.ResponseEnum;
import com.atguigu.srb.common.utils.RegexValidateUtils;
import com.atguigu.srb.sms.client.CoreUserInfoClient;
import com.atguigu.srb.sms.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * projectName: srb
 *
 * @author: GOD伟
 * time: 2022/6/19 17:46 周日
 * description:
 */
@RequestMapping("api/sms")
@RestController
//@CrossOrigin
public class ApiSmsController {
    @Autowired
    SmsService smsService;

    @Autowired
    CoreUserInfoClient coreUserInfoClient;

    /*发送短信接口*/
    @RequestMapping("send/{mobile}")
    public R send(@PathVariable("mobile") String mobile) {
        //非空校验
        Assert.notNull(mobile, ResponseEnum.MOBILE_NULL_ERROR);
        //手机格式检验
        Assert.isTrue(RegexValidateUtils.checkCellphone(mobile), ResponseEnum.MOBILE_ERROR);
        //校验手机是否被注册
        boolean b = coreUserInfoClient.checkMobile(mobile);
        Assert.isTrue(b, ResponseEnum.MOBILE_EXIST_ERROR);

        smsService.send(mobile);
        return R.ok();
    }


}
