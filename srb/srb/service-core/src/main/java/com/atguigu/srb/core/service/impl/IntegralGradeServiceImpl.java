package com.atguigu.srb.core.service.impl;

import com.atguigu.srb.core.mapper.UserInfoMapper;
import com.atguigu.srb.core.pojo.entity.IntegralGrade;
import com.atguigu.srb.core.mapper.IntegralGradeMapper;
import com.atguigu.srb.core.pojo.entity.UserInfo;
import com.atguigu.srb.core.service.IntegralGradeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * <p>
 * 积分等级表 服务实现类
 * </p>
 *
 * @author God伟
 * @since 2022-06-12
 */
@Service
public class IntegralGradeServiceImpl extends ServiceImpl<IntegralGradeMapper, IntegralGrade> implements IntegralGradeService {

    @Autowired
    UserInfoMapper userInfoMapper;

    @Override
    public BigDecimal getBorrowAmount(String userId) {
        UserInfo userInfo = userInfoMapper.selectById(userId);
        Integer integral = userInfo.getIntegral();

        QueryWrapper<IntegralGrade> integralGradeQueryWrapper = new QueryWrapper<>();
        integralGradeQueryWrapper.le("integral_start", integral);
        integralGradeQueryWrapper.gt("integral_end", integral);

        IntegralGrade integralGrade = baseMapper.selectOne(integralGradeQueryWrapper);

        return integralGrade.getBorrowAmount();
    }
}
