package com.atguigu.srb.core.service;

import com.atguigu.srb.core.pojo.entity.IntegralGrade;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * <p>
 * 积分等级表 服务类
 * </p>
 *
 * @author God伟
 * @since 2022-06-12
 */
public interface IntegralGradeService extends IService<IntegralGrade> {

    BigDecimal getBorrowAmount(String userId);
}
