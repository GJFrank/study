package com.atguigu.srb.core.controller.api;


import com.atguigu.srb.common.result.R;
import com.atguigu.srb.core.service.IntegralGradeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * <p>
 * 积分等级表 前端控制器
 * </p>
 *
 * @author God伟
 * @since 2022-06-12
 */
@RestController
@RequestMapping("/api/core/integralGrade")
public class ApiIntegralGradeController {

    @Autowired
    IntegralGradeService integralGradeService;

    @ApiOperation("获取借款额度")
    @GetMapping("/getBorrowAmount")
    public R getBorrowAmount(HttpServletRequest request) {
        String userId = request.getHeader("userId");
        BigDecimal borrowAmount= integralGradeService.getBorrowAmount(userId);
        return R.ok().data("borrowAmount",borrowAmount);

    }
}

