package com.atguigu.srb.core.controller.api;

import com.atguigu.srb.base.util.JwtUtils;
import com.atguigu.srb.common.exception.Assert;
import com.atguigu.srb.common.result.R;
import com.atguigu.srb.common.result.ResponseEnum;
import com.atguigu.srb.core.pojo.vo.LoginVO;
import com.atguigu.srb.core.pojo.vo.RegisterVO;
import com.atguigu.srb.core.pojo.vo.UserInfoVO;
import com.atguigu.srb.core.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * projectName: srb
 *
 * @author: GOD伟
 * time: 2022/6/21 21:52 周二
 * description:
 */
@RestController
@RequestMapping("/api/core/userInfo")
//@CrossOrigin
public class ApiUserInfoController {

    @Autowired
    UserInfoService userInfoService;

    @PostMapping("/register")
    public R register(@RequestBody RegisterVO registerVO) {

        //调用业务层
        userInfoService.register(registerVO);
        return R.ok();
    }

    @PostMapping("/login")
    public R login(@RequestBody LoginVO loginVO, HttpServletRequest request) {

        String header = request.getHeader("X-forwarded-for");
        UserInfoVO userInfoVO = userInfoService.login(loginVO, header);
        return R.ok().data("userInfo", userInfoVO);
    }

    @GetMapping("/checkToken")
    public R checkToken(HttpServletRequest request){
        String token = request.getHeader("token");
        // 调用业务层
        boolean b = JwtUtils.checkToken(token);
        Assert.isTrue(b, ResponseEnum.LOGIN_AUTH_ERROR);
        return R.ok();
    }

    @GetMapping("checkMobile/{mobile}")
    boolean checkMobile(@PathVariable("mobile") String mobile){
        boolean b = userInfoService.checkMobile(mobile);
        return b; //true为不存在
    }
}
