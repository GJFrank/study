package com.atguigu.srb.core.controller.api;


import com.alibaba.fastjson.JSON;
import com.atguigu.srb.common.exception.Assert;
import com.atguigu.srb.common.result.R;
import com.atguigu.srb.common.result.ResponseEnum;
import com.atguigu.srb.common.utils.JwtUtils;
import com.atguigu.srb.core.hfb.RequestHelper;
import com.atguigu.srb.core.service.TransFlowService;

import com.atguigu.srb.core.service.UserAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.ResultMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 * 用户账户 前端控制器
 * </p>
 *
 * @author God伟
 * @since 2022-06-12
 */
@Api(tags = "会员账户")
@RestController
@RequestMapping("/api/core/userAccount")
@Slf4j
public class ApiUserAccountController {
    @Autowired
    UserAccountService userAccountService;
    @Autowired
    TransFlowService transFlowService;

    @ApiOperation("查询账户余额")
    @GetMapping("/auth/getAccount")
    public R getAccount(HttpServletRequest request){
        String userId = request.getHeader("userId");
      BigDecimal account=  userAccountService.getAccount(userId);
      return R.ok().data("account",account);
    }

    @ApiOperation("充值")
    @RequestMapping("/auth/commitCharge/{chargeAmt}")
    public R commitCharge(
            @ApiParam(value = "充值金额", required = true)
            @PathVariable BigDecimal chargeAmt, HttpServletRequest request) {
        String userId = request.getHeader("userId");
        String formStr = userAccountService.commitCharge(chargeAmt, userId);
        return R.ok().data("formStr", formStr);
    }

    @ApiOperation("用户充值异步回调")
    @PostMapping("/notify")
    public String notify(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> paramMap = RequestHelper.switchMap(parameterMap);
        log.info("用户充值异步回调:" + JSON.toJSONString(paramMap));

        //交易的幂等性检查
        String agentBillNo = (String) paramMap.get("agentBillNo");
        boolean saveTransFlow = transFlowService.isSaveTransFlow(agentBillNo);
        Assert.isTrue(!saveTransFlow, ResponseEnum.TRANS_FINISH);

        //汇付宝绑定回调接口
        userAccountService.notifyAccount(paramMap);
        return "success";
    }

}

