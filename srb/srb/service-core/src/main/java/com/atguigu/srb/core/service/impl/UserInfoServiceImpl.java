package com.atguigu.srb.core.service.impl;

import com.atguigu.srb.base.util.JwtUtils;
import com.atguigu.srb.common.exception.Assert;
import com.atguigu.srb.common.result.ResponseEnum;
import com.atguigu.srb.common.utils.MD5;
import com.atguigu.srb.common.utils.RegexValidateUtils;
import com.atguigu.srb.core.mapper.UserAccountMapper;
import com.atguigu.srb.core.mapper.UserLoginRecordMapper;
import com.atguigu.srb.core.pojo.entity.UserAccount;
import com.atguigu.srb.core.pojo.entity.UserInfo;
import com.atguigu.srb.core.mapper.UserInfoMapper;
import com.atguigu.srb.core.pojo.entity.UserLoginRecord;
import com.atguigu.srb.core.pojo.vo.LoginVO;
import com.atguigu.srb.core.pojo.vo.RegisterVO;
import com.atguigu.srb.core.pojo.vo.UserInfoVO;
import com.atguigu.srb.core.service.UserInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户基本信息 服务实现类
 * </p>
 *
 * @author God伟
 * @since 2022-06-12
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    UserAccountMapper userAccountMapper;

    @Autowired
    UserLoginRecordMapper userLoginRecordMapper;

    @Override
    public void register(RegisterVO registerVO) {
        //创建对象接受vo信息, 用于验证
        String mobile = registerVO.getMobile();
        String code = registerVO.getCode();
        String password = registerVO.getPassword();
        Integer userType = registerVO.getUserType();

        //为空检测
        Assert.notNull(mobile, ResponseEnum.MOBILE_NULL_ERROR);
        Assert.notNull(code, ResponseEnum.CODE_NULL_ERROR);
        Assert.isTrue(RegexValidateUtils.checkCellphone(mobile), ResponseEnum.MOBILE_ERROR);//验证手机号码格式

        //手机号码是否已注册
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();//新建一个查询包装对象
        userInfoQueryWrapper.eq("mobile", mobile);//加入条件, 手机号为当前的手机号
        Integer integer = baseMapper.selectCount(userInfoQueryWrapper); //执行选择语句.使用selectCount进行计数
        Assert.isTrue(integer <= 0, ResponseEnum.MOBILE_EXIST_ERROR);//<=0时表示没有,否则就是已经存在了

        //验证码是否正确
        String cacheCode = (String) redisTemplate.opsForValue().get("srb:sms:code:" + mobile);
        Assert.equals(cacheCode, code, ResponseEnum.CODE_ERROR);

        //生成userInfo
        UserInfo userInfo = new UserInfo();
        userInfo.setMobile(mobile);
        userInfo.setName(mobile);
        userInfo.setNickName(mobile);
        userInfo.setUserType(userType);
        userInfo.setPassword(MD5.encrypt(password));
        baseMapper.insert(userInfo);

        //生成userAccount
        UserAccount userAccount = new UserAccount();
        userAccount.setUserId(userInfo.getId());
        userAccountMapper.insert(userAccount);

    }

    @Override
    public UserInfoVO login(LoginVO loginVO, String ip) {
        UserInfoVO userInfoVO = new UserInfoVO();

        //校验参数
        String mobile = loginVO.getMobile();
        Integer userType = loginVO.getUserType();
        String password = loginVO.getPassword();
        Assert.notNull(mobile, ResponseEnum.MOBILE_NULL_ERROR);
        Assert.notNull(password, ResponseEnum.PASSWORD_NULL_ERROR);

        //校验用户名和密码
        //创建一个条件构造器对象
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("user_type", userType);
        userInfoQueryWrapper.eq("mobile", mobile);
        UserInfo userInfo = baseMapper.selectOne(userInfoQueryWrapper);
        Assert.notNull(userInfo, ResponseEnum.LOGIN_MOBILE_ERROR);

        //加密后的密码
        String passwordDb = userInfo.getPassword();
        //对获取的明文密码进行加密, 与数据库中的加密密码进行比对,
        Assert.equals(MD5.encrypt(password), passwordDb, ResponseEnum.LOGIN_PASSWORD_ERROR);

        //制作token  这里直接使用的是getId,getName,
        // 一般是传入整个userInfo给工具包的createToken方法 ,不对外透明的
        String token = JwtUtils.createToken(userInfo.getId(), userInfo.getName());

        //记录用户登录日志
        UserLoginRecord userLoginRecord = new UserLoginRecord();
        userLoginRecord.setUserId(userInfo.getId());
        userLoginRecord.setIp(ip);
        userLoginRecordMapper.insert(userLoginRecord);

        //用户前端对象
        userInfoVO.setToken(token);
        userInfoVO.setMobile(userInfo.getMobile());
        userInfoVO.setName(userInfo.getName());
        userInfoVO.setNickName(userInfo.getNickName());
        userInfoVO.setUserType(userInfo.getUserType());

        //返回值
        return userInfoVO;
    }

    @Override
    public boolean checkMobile(String mobile) {
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("mobile", mobile);
        Integer integer = baseMapper.selectCount(userInfoQueryWrapper);
        return integer <= 0;
    }

    @Override
    public String getMobileByBindCode(String bindCode) {
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("bind_code", bindCode);
        UserInfo userInfo = baseMapper.selectOne(userInfoQueryWrapper);
        return userInfo.getMobile();


    }
}
