package com.atguigu.srb.core.service.impl;

import com.atguigu.srb.common.exception.Assert;
import com.atguigu.srb.common.result.ResponseEnum;
import com.atguigu.srb.core.enums.BorrowInfoStatusEnum;
import com.atguigu.srb.core.enums.BorrowerStatusEnum;
import com.atguigu.srb.core.mapper.UserInfoMapper;
import com.atguigu.srb.core.pojo.entity.BorrowInfo;
import com.atguigu.srb.core.mapper.BorrowInfoMapper;
import com.atguigu.srb.core.pojo.entity.Borrower;
import com.atguigu.srb.core.pojo.entity.UserInfo;

import com.atguigu.srb.core.pojo.vo.BorrowInfoApprovalVO;
import com.atguigu.srb.core.pojo.vo.BorrowerDetailVO;
import com.atguigu.srb.core.service.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 借款信息表 服务实现类
 * </p>
 *
 * @author God伟
 * @since 2022-06-12
 */
@Service
public class BorrowInfoServiceImpl extends ServiceImpl<BorrowInfoMapper, BorrowInfo> implements BorrowInfoService {

    @Autowired
    IntegralGradeService integralGradeService;
    @Autowired
    DictService dictService;
    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    BorrowerService borrowerService;
    @Autowired
    LendService lendService;

    @Override
    public Integer getBorrowInfoStatus(String userId) {
        QueryWrapper<BorrowInfo> borrowInfoQueryWrapper = new QueryWrapper<>();
        borrowInfoQueryWrapper.eq("user_id", userId);
        BorrowInfo borrowInfo = baseMapper.selectOne(borrowInfoQueryWrapper);
        if (null == borrowInfo) {
            return 0;
        }
        return borrowInfo.getStatus();
    }

    @Override
    public void saveBorrowInfo(BorrowInfo borrowInfo) {
        //判断借款额度是否足够
        BigDecimal borrowAmount = integralGradeService.getBorrowAmount(borrowInfo.getUserId().toString());
        int i = borrowAmount.compareTo(borrowInfo.getAmount());
        Assert.isTrue(i >= 0, ResponseEnum.USER_AMOUNT_LESS_ERROR);
        borrowInfo.setStatus(BorrowerStatusEnum.AUTH_RUN.getStatus());
        borrowInfo.setBorrowYearRate(borrowAmount.divide(new BigDecimal("100")));
        baseMapper.insert(borrowInfo);
    }

    @Override
    public List<BorrowInfo> selectAllList() {
        List<BorrowInfo> borrowInfoList = baseMapper.selectList(null);
        borrowInfoList.forEach(borrowInfo -> {
            UserInfo userInfo = userInfoMapper.selectById(borrowInfo.getUserId());
            borrowInfo.getParam().put("name", userInfo.getName());
            borrowInfo.getParam().put("mobile", userInfo.getMobile());
            String returnMethod = dictService.getNameByParentDictCodeAndValue("returnMethod", borrowInfo.getReturnMethod());
            String moneyUse = dictService.getNameByParentDictCodeAndValue("moneyUse", borrowInfo.getMoneyUse());
            String msgByStatus = BorrowInfoStatusEnum.getMsgByStatus(borrowInfo.getStatus());
            borrowInfo.getParam().put("returnMethod", returnMethod);
            borrowInfo.getParam().put("moneyUse", moneyUse);
            borrowInfo.getParam().put("status", msgByStatus);

        });
        return borrowInfoList;
    }

    @Override
    public Map<String, Object> getBorrowInfoDetail(Long borrowInfoId) {
        Map<String, Object> borrowInfoDetail = new HashMap<>();

        //借款信息
        BorrowInfo borrowInfo = baseMapper.selectById(borrowInfoId);
        borrowInfo.getParam().put("returnMethod", dictService.getNameByParentDictCodeAndValue("returnMethod", borrowInfo.getReturnMethod()));
        borrowInfo.getParam().put("moneyUse", dictService.getNameByParentDictCodeAndValue("moneyUse", borrowInfo.getMoneyUse()));
        borrowInfo.getParam().put("status", dictService.getNameByParentDictCodeAndValue("moneyUse", borrowInfo.getMoneyUse()));

        //借款人信息
        Long userId = borrowInfo.getUserId();
        QueryWrapper<Borrower> borrowerQueryWrapper = new QueryWrapper<>();
        borrowerQueryWrapper.eq("user_id", userId);
        Borrower one = borrowerService.getOne(borrowerQueryWrapper);
        //组装借款人对象
        BorrowerDetailVO borrowerDetailVO = borrowerService.getBorrowerDetailVOById(one.getId());

        //返回borrowInfoDeatil
        borrowInfoDetail.put("borrower", borrowerDetailVO);
        borrowInfoDetail.put("borrowInfo", borrowInfo);
        return borrowInfoDetail;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)  //申明回滚条件
    public void approval(BorrowInfoApprovalVO borrowInfoApprovalVO) {
        //修改借款信息状态
        Long borrowInfoApprovalVOId = borrowInfoApprovalVO.getId();
        BorrowInfo borrowInfo = baseMapper.selectById(borrowInfoApprovalVOId);
        borrowInfo.setStatus(borrowInfoApprovalVO.getStatus());
        baseMapper.updateById(borrowInfo);

        //审核通过则创建标的
        if (borrowInfoApprovalVO.getStatus().intValue() == BorrowInfoStatusEnum.CHECK_OK.getStatus().intValue()) {
            //创建标.
            lendService.createLend(borrowInfoApprovalVO,borrowInfo);
            System.out.println("开始创建标的 TODO");
        }
    }
}
