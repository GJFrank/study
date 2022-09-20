package com.atguigu.srb.core.controller.api;


import com.atguigu.srb.common.result.R;
import com.atguigu.srb.core.pojo.vo.BorrowerVO;
import com.atguigu.srb.core.service.BorrowerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 借款人 前端控制器
 * </p>
 *
 * @author God伟
 * @since 2022-06-12
 */
@Api(tags = "借款信息")
@RestController
@RequestMapping("/api/core/borrower")
@Slf4j
public class ApiBorrowerController {

    @Autowired
    BorrowerService borrowerService;

    @GetMapping("/getBorrowStatus")
    public R getBorrowStatus(HttpServletRequest request) {
        String userId = request.getHeader("userId");
        Integer borrowerStatus = borrowerService.getBorrowerStatus(userId);
        return R.ok().data("borrowerStatus", borrowerStatus);
    }

    @PostMapping("/auth/save")
    public R saveBorrower(@RequestBody BorrowerVO borrowerVO, HttpServletRequest request) {
        String userId = request.getHeader("userId");
        //保存实现方法
        borrowerService.saveBorrower(borrowerVO, userId);
        return R.ok();
    }



}

