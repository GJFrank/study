package com.atguigu.srb.core.service.impl;

import com.atguigu.srb.common.exception.Assert;
import com.atguigu.srb.common.result.ResponseEnum;
import com.atguigu.srb.core.enums.UserBindEnum;
import com.atguigu.srb.core.hfb.FormHelper;
import com.atguigu.srb.core.hfb.HfbConst;
import com.atguigu.srb.core.hfb.RequestHelper;
import com.atguigu.srb.core.mapper.UserInfoMapper;
import com.atguigu.srb.core.pojo.entity.UserBind;
import com.atguigu.srb.core.mapper.UserBindMapper;
import com.atguigu.srb.core.pojo.entity.UserInfo;
import com.atguigu.srb.core.pojo.vo.UserBindVO;
import com.atguigu.srb.core.service.UserBindService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户绑定表 服务实现类
 * </p>
 *
 * @author God伟
 * @since 2022-06-12
 */
@Service
public class UserBindServiceImpl extends ServiceImpl<UserBindMapper, UserBind> implements UserBindService {

    @Autowired
    UserInfoMapper userInfoMapper;

    @Override
    public String commitBind(UserBindVO userBindVO, String userId) {
        //将用户绑定信息保存userBind表
        //判断之前是否提交过绑定信息
        QueryWrapper<UserBind> userBindQueryWrapper = new QueryWrapper<>();
        userBindQueryWrapper.eq("user_id", userId);
        UserBind userBind = baseMapper.selectOne(userBindQueryWrapper);

        if (userBind != null) {
            //之前提交过绑定请求, 本次修改
            BeanUtils.copyProperties(userBindVO, userBind);
            baseMapper.updateById(userBind);
        } else {
            //之前未提交过绑定请求, 本次保存
            UserBind userBindForSave = new UserBind();
            userBindForSave.setBankNo(userBindVO.getBankNo());
            userBindForSave.setBankType(userBindVO.getBankType());
            userBindForSave.setIdCard(userBindVO.getIdCard());
            userBindForSave.setMobile(userBindVO.getMobile());
            userBindForSave.setName(userBindVO.getName());
            userBindForSave.setStatus(UserBindEnum.NO_BIND.getStatus());
            userBindForSave.setUserId(Long.parseLong(userId));
            baseMapper.insert(userBindForSave);
        }

        //调用汇付宝接口
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("agentId", HfbConst.AGENT_ID);
        paramMap.put("agentUserId", userId);
        paramMap.put("idCard", userBindVO.getIdCard());
        paramMap.put("personalName", userBindVO.getName());
        paramMap.put("bankType", userBindVO.getBankType());
        paramMap.put("bankNo", userBindVO.getBankNo());
        paramMap.put("mobile", userBindVO.getMobile());
        paramMap.put("returnUrl", HfbConst.USERBIND_RETURN_URL);
        paramMap.put("notifyUrl", HfbConst.USERBIND_NOTIFY_URL);
        paramMap.put("timestamp", RequestHelper.getTimestamp());
        paramMap.put("sign", RequestHelper.getSign(paramMap));

        //构建充值自动提交表单
        String form = FormHelper.buildForm(HfbConst.USERBIND_URL, paramMap);
        return form;
    }

    @Override
    public void notifyBind(Map<String, Object> resultMap) {
        boolean signEquals = RequestHelper.isSignEquals(resultMap);
        Assert.isTrue(signEquals, ResponseEnum.PAY_UNIFIEDORDER_ERROR);
        String userId = (String) resultMap.get("agentUserId");
        String bindCode = (String) resultMap.get("bindCode");

        //修改绑定信息, 保存绑定码 bind_code
        QueryWrapper<UserBind> userBindQueryWrapper = new QueryWrapper<>();
        userBindQueryWrapper.eq("user_id", userId);
        UserBind userBind = baseMapper.selectOne(userBindQueryWrapper);
        userBind.setBindCode(bindCode);
        userBind.setStatus(UserBindEnum.BIND_OK.getStatus());
        baseMapper.updateById(userBind);

        //修改用户信息, 添加昵称,姓名 身份证, bind_core, 修改绑定状态 bind_status
        UserInfo userInfo = userInfoMapper.selectById(userId);
        userInfo.setNickName(userBind.getName());
        userInfo.setName(userBind.getName());
        userInfo.setIdCard(userBind.getIdCard());
        userInfo.setBindCode(bindCode);
        userInfo.setBindStatus(UserBindEnum.BIND_OK.getStatus());
        userInfoMapper.updateById(userInfo);

    }

    @Override
    public String getBindCodeByUserId(Long investUserId) {
        QueryWrapper<UserBind> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", investUserId);
        UserBind userBind = baseMapper.selectOne(queryWrapper);
        String bindCode = userBind.getBindCode();
        return bindCode;
    }
}
