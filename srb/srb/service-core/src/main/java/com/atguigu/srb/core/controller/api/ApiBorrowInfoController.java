package com.atguigu.srb.core.controller.api;


import com.atguigu.srb.common.result.R;
import com.atguigu.srb.core.pojo.entity.BorrowInfo;
import com.atguigu.srb.core.service.BorrowInfoService;
import com.atguigu.srb.core.service.BorrowerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 * 借款信息表 前端控制器
 * </p>
 *
 * @author God伟
 * @since 2022-06-12
 */
@RestController
@RequestMapping("/api/core/borrowInfo")
public class ApiBorrowInfoController {

    @Autowired
    BorrowInfoService borrowInfoService;

    @PostMapping("/auth/save")
    public R saveBorrowInfo(@RequestBody BorrowInfo borrowInfo, HttpServletRequest request) {
        System.out.println(borrowInfo);
        String userId = request.getHeader("userId");
        borrowInfo.setUserId(Long.parseLong(userId));
        borrowInfoService.saveBorrowInfo(borrowInfo);
        return R.ok();
    }

    @GetMapping("getBorrowInfoStatus")
    public R getBorroInfoStatus(HttpServletRequest request) {
        String userId = request.getHeader("userId");

        Integer borrowInfoStatus = borrowInfoService.getBorrowInfoStatus(userId);
        return R.ok().data("borrowInfoStatus", borrowInfoStatus);
    }



}

