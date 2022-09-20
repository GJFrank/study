package com.atguigu.srb.core.controller.admin;

import com.atguigu.srb.common.exception.Assert;
import com.atguigu.srb.common.exception.BusinessException;
import com.atguigu.srb.common.result.R;
import com.atguigu.srb.common.result.ResponseEnum;
import com.atguigu.srb.core.pojo.entity.IntegralGrade;
import com.atguigu.srb.core.service.IntegralGradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * projectName: srb
 *
 * @author: GOD伟
 * time: 2022/6/12 23:49 周日
 * description:
 * @Api 接口方法注解
 * @Option 方法注解
 * @ApiParam 参数注解
 * @ApiModel 映射类注解
 * @ApiModelProperty 映射类属性注解
 */
//@CrossOrigin
@RestController
@RequestMapping("/admin/core/integralGrade")
@Api(tags = "积分等级管理接口")
@Slf4j
public class AdminIntegralGradeController {
    @Autowired
    private IntegralGradeService integralGradeService;


    @ApiOperation(value = "删除积分等级接口", notes = "逻辑删除")
    @DeleteMapping("/remove/{id}")
    public R removeById(
            @ApiParam(value = "积分等级主键", required = true, example = "1")
            @PathVariable Long id) {
        boolean b = integralGradeService.removeById(id);
        if (b) {
            return R.ok().message("删除成功");
        } else {
            return R.error().message("删除失败");
        }
    }


    @ApiOperation("积分等级列表")
    @GetMapping("/list")
    public R listAll() {
        log.trace("最详细");
        log.debug("debug日志");
        log.info("基本信息日志");
        log.warn("警告日志");
        log.error("错误日志");
        List<IntegralGrade> list = integralGradeService.list();
        return R.ok().data("list", list);
    }

    @GetMapping("listAll")
    public R listAll(HttpServletRequest request){  //request:RequestFacade
        String remoteAddr = request.getRemoteAddr(); //获得请求用户浏览器ip地址  remoteAddr:"0:0:0:0:0:0:0:1"

        String header = request.getHeader("X-forwarded-for");
        System.out.println(Thread.currentThread().getName());

        log.trace("最详细");
        log.debug("debug日志");
        log.info("基本信息日志");
        log.warn("警告日志");
        log.error("错误日志");
        List<IntegralGrade> list = integralGradeService.list();
        return R.ok().data("list", list);
    }

    @ApiOperation("新增积分等级")
    @PostMapping("/save")
    public R save(
            @ApiParam(value = "积分等级对象", required = true)
            @RequestBody IntegralGrade integralGrade) {
        /*如果借款额度为空,就手动抛出一个自定义的异常*/
        BigDecimal amount = integralGrade.getBorrowAmount();
        Assert.notNull(amount, ResponseEnum.BORROW_AMOUNT_NULL_ERROR);

        //借款额度不能小于等于0
        int i = amount.compareTo(new BigDecimal("0"));  //0 相等  1前数大于后数  -1 前数小于后数
        Assert.isTrue(i == 1, ResponseEnum.BORROW_AMOUNT_ZERO_ERROR);
        this.integralGradeService.save(integralGrade);
        return R.ok();
    }

    @ApiOperation("根据id获取积分等级")
    @GetMapping("/get/{id}")
    public R getById(
            @ApiParam(value = "数据id", required = true, example = "1")
            @PathVariable Long id
    ) {
        IntegralGrade integralGrade = integralGradeService.getById(id);
        if (integralGrade != null) {
            return R.ok().data("record", integralGrade);
        } else {
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("更新积分等级")
    @PutMapping("/update")
    public R updateById(

            @ApiParam(value = "积分等级对象", required = true)
            @RequestBody IntegralGrade integralGrade) {
        boolean result = integralGradeService.updateById(integralGrade);
        if (result) {
            return R.ok().message("修改成功");
        } else {
            return R.error().message("修改失败");
        }
    }
}
