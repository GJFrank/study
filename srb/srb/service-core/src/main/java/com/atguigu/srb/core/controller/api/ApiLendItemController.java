package com.atguigu.srb.core.controller.api;


import com.alibaba.fastjson.JSON;
import com.atguigu.srb.common.result.R;
import com.atguigu.srb.core.hfb.RequestHelper;
import com.atguigu.srb.core.pojo.entity.UserInfo;
import com.atguigu.srb.core.pojo.vo.InvestVO;
import com.atguigu.srb.core.service.LendItemService;
import com.atguigu.srb.core.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 标的出借记录表 前端控制器
 * </p>
 *
 * @author God伟
 * @since 2022-06-12
 */
@Api(tags = "标的的投资")
@Slf4j
@RestController
@RequestMapping("/api/core/lendItem")
public class ApiLendItemController {

    @Autowired
    LendItemService lendItemService;

    @Autowired
    UserInfoService userInfoService;

    @ApiOperation("会员投资提交数据")
    @PostMapping("/auth/commitInvest")
    public R commitInvest(@RequestBody InvestVO investVO, HttpServletRequest request) {
        String userId = request.getHeader("userId");
        Long aLong = Long.parseLong(userId);
        UserInfo userInfo = userInfoService.getById(userId);
        String userName = userInfo.getName();
        investVO.setInvestUserId(aLong);
        investVO.setInvestName(userName);

        //构建自动提交表单
        String formStr = lendItemService.commitInvest(investVO);
        return R.ok().data("formStr", formStr);

    }

    @ApiOperation("会员投资异步回调")
    @PostMapping("/notify")
    public String notify(HttpServletRequest request) {
        Map<String, Object> paramMap = RequestHelper.switchMap(request.getParameterMap());
        System.out.println("会员投资异步回调 param : " + JSON.toJSONString(paramMap));  //打印测试

        //校验签名 P2pInvestNotifyVO
        if (RequestHelper.isSignEquals(paramMap)) {
            if ("0001".equals(paramMap.get("resultCode"))) {
                lendItemService.notify(paramMap);
            } else {
                log.info("用户投资异步回调失败：" + JSON.toJSONString(paramMap));
                return "fail";
            }
        } else {
            log.info("用户投资异步回调签名错误：" + JSON.toJSONString(paramMap));
            return "fail";
        }
        return "success";
    }


}


