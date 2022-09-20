package com.atguigu.srb.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.srb.base.dto.SmsDTO;
import com.atguigu.srb.core.enums.TransTypeEnum;
import com.atguigu.srb.core.hfb.FormHelper;
import com.atguigu.srb.core.hfb.HfbConst;
import com.atguigu.srb.core.hfb.RequestHelper;
import com.atguigu.srb.core.mapper.UserInfoMapper;
import com.atguigu.srb.core.pojo.bo.TransFlowBO;

import com.atguigu.srb.core.pojo.entity.UserAccount;
import com.atguigu.srb.core.mapper.UserAccountMapper;
import com.atguigu.srb.core.pojo.entity.UserInfo;
import com.atguigu.srb.core.service.TransFlowService;
import com.atguigu.srb.core.service.UserAccountService;
import com.atguigu.srb.core.service.UserInfoService;
import com.atguigu.srb.core.utils.LendNoUtils;

import com.atguigu.srb.rabbitmq.constant.MQConst;
import com.atguigu.srb.rabbitmq.service.MQService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Reference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户账户 服务实现类
 * </p>
 *
 * @author God伟
 * @since 2022-06-12
 */
@Service
@Slf4j
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccount> implements UserAccountService {

    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    UserAccountMapper userAccountMapper;

    @Autowired
    TransFlowService transFlowService;

    @Autowired
    UserInfoService userInfoService;
    @Autowired
    MQService mqService;

    @Override
    public String commitCharge(BigDecimal chargeAmt, String userId) {
        UserInfo userInfo = userInfoMapper.selectById(userId);
        String bindCode = userInfo.getBindCode();

        //判断用户绑定状态是否为空
        //  Assert.notEmpty(bindCode, ResponseEnum.USER_NO_BIND_ERROR);

        //根据第三方充值接口文档进行填写
        Map<String, Object> paramMap = new HashMap<>();

        paramMap.put("agentId", HfbConst.AGENT_ID);
        paramMap.put("agentBillNo", LendNoUtils.getNo());
        paramMap.put("bindCode", bindCode);
        paramMap.put("chargeAmt", chargeAmt);
        paramMap.put("feeAmt", new BigDecimal(0)); //商户收取用户的手续费, 对于尚融宝来说, 充值时还是不要设置手续费好
        paramMap.put("notifyUrl", HfbConst.RECHARGE_NOTIFY_URL);
        paramMap.put("returnUrl", HfbConst.RECHARGE_RETURN_URL);
        paramMap.put("timestamp", RequestHelper.getTimestamp());
        String sign = RequestHelper.getSign(paramMap);
        paramMap.put("sign", sign);

        //构建充值自动提交表单
        String formStr = FormHelper.buildForm(HfbConst.RECHARGE_URL, paramMap);
        return formStr;

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void notifyAccount(Map<String, Object> paramMap) {
        log.info("充值成功:" + JSONObject.toJSONString(paramMap));

        String bindCode = (String) paramMap.get("bindCode");  //充值人绑定协议号
        String chargeAmt = (String) paramMap.get("chargeAmt");//充值金额
        String agentBillNo = (String) paramMap.get("agentBillNo");//商户充值订单号

  /*      QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("bind_code", bindCode);
        UserInfo userInfo = userInfoMapper.selectOne(userInfoQueryWrapper);
*/
        //判断交易流水是否存在
        boolean isSaved = transFlowService.isSaveTransFlow(agentBillNo);
        if (isSaved) {
            log.warn("幂等性返回");
            return;
        }

        //更新账户信息
        baseMapper.updateAccount(bindCode, new BigDecimal(chargeAmt), new BigDecimal(0));

        //增加交易流水
        //String agentBillNo = (String) paramMap.get("agentBillNo"); //商户充值订单号
        TransFlowBO transFlowBO = new TransFlowBO(
                agentBillNo,
                bindCode,
                new BigDecimal(chargeAmt),
                TransTypeEnum.RECHARGE,
                "充值");
        transFlowService.saveTransFlow(transFlowBO);

        //发消息
        log.info("发消息");
        // String mobileByBindCode = userInfoService.getMobileByBindCode(bindCode);
        String mobileByBindCode = "13666594295";
        SmsDTO smsDTO = new SmsDTO();
        smsDTO.setMobile(mobileByBindCode);
        smsDTO.setMessage("充值成功");
        mqService.sendMessage("exchange.topic.sms", MQConst.ROUTING_SMS_ITEM, smsDTO);
    }

    @Override
    public BigDecimal getAccount(String userId) {
        QueryWrapper<UserAccount> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("user_id", userId);
        UserAccount userAccount = baseMapper.selectOne(queryWrapper);

        BigDecimal amount = userAccount.getAmount();
        return amount;
    }
}

