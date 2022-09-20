package com.atguigu.srb.core.controller.admin;


import com.atguigu.srb.common.result.R;
import com.atguigu.srb.core.pojo.entity.BorrowInfo;
import com.atguigu.srb.core.pojo.vo.BorrowInfoApprovalVO;
import com.atguigu.srb.core.service.BorrowInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 借款信息表 前端控制器
 * </p>
 *
 * @author God伟
 * @since 2022-06-12
 */
@Api(tags = "借款管理")
@RestController
@RequestMapping("/admin/core/borrowInfo")
@Slf4j
public class AdminBorrowInfoController {
    @Autowired
    BorrowInfoService borrowInfoService;

    @ApiOperation("审批借款信息")
    @PostMapping("/approval")
    public R approval(@RequestBody BorrowInfoApprovalVO borrowInfoApprovalVO){

        //调用业务层提交借款审核信息
        borrowInfoService.approval(borrowInfoApprovalVO);
        return  R.ok().message("审批完成");
    }

    @ApiOperation("借款信息列表")
    @GetMapping("list")
    public R borrowInfoList() {
        List<BorrowInfo> borrowInfoList = borrowInfoService.selectAllList();
        return R.ok().data("list", borrowInfoList);

    }
    @ApiOperation("借款详情页")
    @GetMapping("show/{borrowInfoId}")
    public R showBorrowInfoDetail(@PathVariable Long borrowInfoId) {
        Map<String, Object> borrowInfoDetail = borrowInfoService.getBorrowInfoDetail(borrowInfoId);
        return R.ok().data("borrowInfoDetail",borrowInfoDetail);
    }

}

