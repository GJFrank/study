package com.atguigu.srb.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.srb.common.exception.BusinessException;
import com.atguigu.srb.core.enums.LendStatusEnum;
import com.atguigu.srb.core.enums.ReturnMethodEnum;
import com.atguigu.srb.core.enums.TransTypeEnum;
import com.atguigu.srb.core.hfb.HfbConst;
import com.atguigu.srb.core.hfb.RequestHelper;
import com.atguigu.srb.core.mapper.BorrowerMapper;
import com.atguigu.srb.core.mapper.UserAccountMapper;
import com.atguigu.srb.core.mapper.UserInfoMapper;
import com.atguigu.srb.core.pojo.bo.TransFlowBO;
import com.atguigu.srb.core.pojo.entity.*;
import com.atguigu.srb.core.mapper.LendMapper;
import com.atguigu.srb.core.pojo.vo.BorrowInfoApprovalVO;
import com.atguigu.srb.core.pojo.vo.BorrowerDetailVO;
import com.atguigu.srb.core.service.*;

import com.atguigu.srb.core.utils.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的准备表 服务实现类
 * </p>
 *
 * @author God伟
 * @since 2022-06-12
 */
@Service
public class LendServiceImpl extends ServiceImpl<LendMapper, Lend> implements LendService {

    @Autowired
    DictService dictService;
    @Autowired
    BorrowerService borrowerService;
    @Autowired
    BorrowerMapper borrowerMapper;
    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    UserAccountMapper userAccountMapper;
    @Autowired
    UserAccountService userAccountService;
    @Autowired
    TransFlowService transFlowService;
    @Autowired
    LendItemService lendItemService;
    @Autowired
    LendReturnService lendReturnService;



    @Override
    public void createLend(BorrowInfoApprovalVO borrowInfoApprovalVO, BorrowInfo borrowInfo) {
        Lend lend = new Lend();
        lend.setUserId(borrowInfo.getUserId());
        lend.setBorrowInfoId(borrowInfo.getId());
        lend.setLendNo(LendNoUtils.getLendNo());  //生成编号
        lend.setTitle(borrowInfoApprovalVO.getTitle());
        lend.setAmount(borrowInfo.getAmount());
        lend.setPeriod(borrowInfo.getPeriod());
        lend.setLendYearRate(borrowInfoApprovalVO.getLendYearRate().divide(new BigDecimal(100)));
        lend.setServiceRate(borrowInfoApprovalVO.getServiceRate().divide(new BigDecimal(100)));
        lend.setReturnMethod(borrowInfo.getReturnMethod());
        lend.setLowestAmount(new BigDecimal(100));
        lend.setInvestAmount(new BigDecimal(0));
        lend.setInvestNum(0);
        lend.setPublishDate(LocalDateTime.now());

        //起息日期
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate lendStartDate = LocalDate.parse(borrowInfoApprovalVO.getLendStartDate(), dateTimeFormatter);
        lend.setLendStartDate(lendStartDate);
        //结束日期
        LocalDate lendEndDate = lendStartDate.plusMonths(borrowInfo.getPeriod());
        lend.setLendEndDate(lendEndDate);

        //描述
        lend.setLendInfo(borrowInfoApprovalVO.getLendInfo());

        //平台预期收益
        //        月年化 = 年化 / 12
        BigDecimal monthRate = lend.getServiceRate().divide(new BigDecimal(12), 8, BigDecimal.ROUND_DOWN);
        //        平台收益 = 标的金额 * 月年化 * 期数
        BigDecimal expectAmount = lend.getAmount().multiply(monthRate).multiply(new BigDecimal(lend.getPeriod()));
        lend.setExpectAmount(expectAmount);

        //实际收益
        lend.setRealAmount(new BigDecimal(0));
        //状态
        lend.setStatus(LendStatusEnum.INVEST_RUN.getStatus());
        //审核时间
        lend.setCheckTime(LocalDateTime.now());
        //审核人
        lend.setCheckAdminId(1L);

        baseMapper.insert(lend);
    }

    @Override
    public List<Lend> selectList() {
        List<Lend> lendList = baseMapper.selectList(null);
        lendList.forEach(lend -> {
            String returnMethod = dictService.getNameByParentDictCodeAndValue("returnMethod", lend.getReturnMethod());
            String msgByStatus = LendStatusEnum.getMsgByStatus(lend.getStatus());
            lend.getParam().put("returnMethod", returnMethod);
            lend.getParam().put("status", msgByStatus);
        });
        return lendList;
    }

    @Override
    public Map<String, Object> getLendDetail(Long id) {
        //查询标的对象
        Lend lend = baseMapper.selectById(id);
        //组装数据
        String returnMethod = dictService.getNameByParentDictCodeAndValue("returnMethod", lend.getReturnMethod());
        String status = LendStatusEnum.getMsgByStatus(lend.getStatus());
        lend.getParam().put("returnMethod", returnMethod);
        lend.getParam().put("status", status);

        //根据user_id获取借款人对象
        QueryWrapper<Borrower> borrowerQueryWrapper = new QueryWrapper<Borrower>();
        borrowerQueryWrapper.eq("user_id", lend.getUserId());
        Borrower borrower = borrowerMapper.selectOne(borrowerQueryWrapper);
        //组装借款人对象
        BorrowerDetailVO borrowerDetailVO = borrowerService.getBorrowerDetailVOById(borrower.getId());

        //组装数据
        Map<String, Object> result = new HashMap<>();
        result.put("lend", lend);
        result.put("borrower", borrowerDetailVO);

        return result;
    }

    @Override
    public BigDecimal getInterestCount(BigDecimal invest, BigDecimal yearRate, Integer totalMonth, Integer returnMethod) {
        BigDecimal interestCount;

        //计算总利息
        if (returnMethod.intValue() == ReturnMethodEnum.ONE.getMethod()) {
            interestCount = Amount1Helper.getInterestCount(invest, yearRate, totalMonth);
        } else if (returnMethod.intValue() == ReturnMethodEnum.TWO.getMethod()) {
            interestCount = Amount2Helper.getInterestCount(invest, yearRate, totalMonth);
        } else if (returnMethod.intValue() == ReturnMethodEnum.THREE.getMethod()) {
            interestCount = Amount3Helper.getInterestCount(invest, yearRate, totalMonth);
        } else {
            interestCount = Amount4Helper.getInterestCount(invest, yearRate, totalMonth);
        }
        return interestCount;
    }

    @Override
    public void makeLoan(Long lendId) {

        //获取标的信息
        Lend lend = baseMapper.selectById(lendId);

        //放款接口调用
        Map<String, Object> paramMap = new HashMap<>();
        //接口文档请求参数
        paramMap.put("agentId", HfbConst.AGENT_ID);  //agent_id 商户唯一标识
        paramMap.put("agentProjectCode", lend.getLendNo());  //agent_project_code 放款项目编号
        String agentBillNo = LendNoUtils.getLoanNo();
        paramMap.put("agentBillNo", agentBillNo);  //agent_bill_no  LendNoUtils工具生成
        //平台收益,放款扣除, 捷库阉人借款实际金额=借款金额-平台收益
        //月年化
        BigDecimal monthRate = lend.getServiceRate().divide(new BigDecimal("12"), 8, BigDecimal.ROUND_DOWN);
        //平台实际收益 = 已投金额*月年化 * 标的期数
        BigDecimal realAmount = lend.getInvestAmount().multiply(monthRate).multiply(new BigDecimal(lend.getPeriod()));

        paramMap.put("mchFee", realAmount);  //mch_fee   商户手续费(平台实际收益)
        paramMap.put("note", "");  //note     放款备注
        paramMap.put("timestamp", RequestHelper.getTimestamp());  //timestamp       时间戳  1970算起的时间戳 RequestHelper提供
        String sign = RequestHelper.getSign(paramMap);
        paramMap.put("sign", sign);  //sign

        System.out.println("放款参数: " + JSON.toJSONString(paramMap));

        //发送同步远程调用
        JSONObject result = RequestHelper.sendRequest(paramMap, HfbConst.MAKE_LOAD_URL);
        System.out.println("放款结果:  " + result.toJSONString());

        //放款失败
        if (!"0000".equals(result.getString("resultCode"))) {
            throw new BusinessException(result.getString("resultMsg"));
        }

        //更新标的信息
        lend.setRealAmount(realAmount);
        lend.setStatus(LendStatusEnum.PAY_RUN.getStatus());
        lend.setPaymentTime(LocalDateTime.now());
        baseMapper.updateById(lend);

        //获取借款人信息
        Long userId = lend.getUserId();
        UserInfo userInfo = userInfoMapper.selectById(userId);
        String bindCode = userInfo.getBindCode();

        //给借款账号转入金额
        BigDecimal voteAmt = new BigDecimal(result.getString("voteAmt"));
        userAccountMapper.updateAccount(bindCode, voteAmt, new BigDecimal("0"));

        //新增借款人交易流水
        TransFlowBO transFlowBO = new TransFlowBO(agentBillNo, bindCode, voteAmt, TransTypeEnum.BORROW_BACK, "借款放款到账,编号" + lend.getLendNo());
        transFlowService.saveTransFlow(transFlowBO);

        //获取投资人列表信息
        List<LendItem> lendItemList = lendItemService.selectByLendId(lendId, 1);
        lendItemList.forEach(lendItem -> {
            //获取投资人信息
            UserInfo investorUserInfo = userInfoMapper.selectById(lendItem.getInvestUserId());
            String investorUserInfoBindCode = investorUserInfo.getBindCode();
            //投资人账号冻结金额转出
            BigDecimal investAmount = lendItem.getInvestAmount();//投资金额
            userAccountMapper.updateAccount(investorUserInfoBindCode, new BigDecimal("0"), investAmount.negate());

            //新增投资人交易流水
            TransFlowBO transFlowBO1 = new TransFlowBO(LendNoUtils.getTransNo(), investorUserInfoBindCode, investAmount, TransTypeEnum.INVEST_UNLOCK, "冻结资金转出,出借放款,编号:" + lend.getLendNo());
            transFlowService.saveTransFlow(transFlowBO1);

            //放款成功 生成借款人还款计划和投资人回款计划

        });

    }

    //放款成功生成借款人还款计划和投资人回款计划
    //TODO
    //生成还款计划
    private void repaymentPlan(Lend lend) {
        //还款计划列表
        List<LendReturn> lendReturnList=new ArrayList<>();
        int len = lend.getPeriod();
        for (int i = 1; i <= len; i++) {
            //创建还款计划对象
            LendReturn lendReturn = new LendReturn();
            lendReturn.setReturnNo(LendNoUtils.getReturnNo());
            lendReturn.setLendId(lend.getId());
            lendReturn.setBorrowInfoId(lend.getBorrowInfoId());
            lendReturn.setUserId(lend.getUserId());
            lendReturn.setAmount(lend.getAmount());
            lendReturn.setBaseAmount(lend.getInvestAmount());
            lendReturn.setLendYearRate(lend.getLendYearRate());
            lendReturn.setCurrentPeriod(i);//当前期数
            lendReturn.setReturnMethod(lend.getReturnMethod());
            lendReturn.setFee(new BigDecimal(0));
            lendReturn.setReturnDate(lend.getLendStartDate().plusMonths(i)); //第二个月开始还款
            lendReturn.setOverdue(false);
            if (i == len) { //最后一个月
                //标识为最后一次还款
                lendReturn.setLast(true);
            } else {
                lendReturn.setLast(false);
            }
            lendReturn.setStatus(0);
            lendReturnList.add(lendReturn);

        }
        //批量保存
        lendReturnService.saveBatch(lendReturnList);



    }

    //生成回款计划

}
