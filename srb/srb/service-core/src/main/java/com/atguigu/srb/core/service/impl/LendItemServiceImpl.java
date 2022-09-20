package com.atguigu.srb.core.service.impl;

import com.atguigu.srb.core.enums.TransTypeEnum;
import com.atguigu.srb.core.hfb.FormHelper;
import com.atguigu.srb.core.hfb.HfbConst;
import com.atguigu.srb.core.hfb.RequestHelper;
import com.atguigu.srb.core.mapper.LendMapper;
import com.atguigu.srb.core.mapper.UserAccountMapper;
import com.atguigu.srb.core.pojo.bo.TransFlowBO;
import com.atguigu.srb.core.pojo.entity.Lend;
import com.atguigu.srb.core.pojo.entity.LendItem;
import com.atguigu.srb.core.mapper.LendItemMapper;
import com.atguigu.srb.core.pojo.entity.UserAccount;
import com.atguigu.srb.core.pojo.vo.InvestVO;
import com.atguigu.srb.core.service.*;
import com.atguigu.srb.core.utils.LendNoUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标的出借记录表 服务实现类
 * </p>
 *
 * @author God伟
 * @since 2022-06-12
 */
@Service
public class LendItemServiceImpl extends ServiceImpl<LendItemMapper, LendItem> implements LendItemService {

    @Autowired
    LendMapper lendMapper;
    @Autowired
    UserAccountService userAccountService;
    @Autowired
    LendService lendService;
    @Autowired
    UserBindService userBindService;
    @Autowired
    TransFlowService transFlowService;
    @Autowired
    UserAccountMapper userAccountMapper;

    @Override
    public String commitInvest(InvestVO investVO) {

        //输入校验*************************
        Long lendId = investVO.getLendId();

        //获取标的信息
        Lend lend = lendMapper.selectById(lendId);

        //标的不能不能超卖  已投金额+本轮投资金额 >=标的金额     超卖
        BigDecimal sum = lend.getInvestAmount().add(new BigDecimal(investVO.getInvestAmount()));

        //账户可用余额充足:
        Long investUserId = investVO.getInvestUserId();
        BigDecimal amount = userAccountService.getAccount(investUserId.toString());


        //在商户平台生成投资信息 *****************
        //标的下的投资信息
        LendItem lendItem = new LendItem();

        String lendItemNo = LendNoUtils.getLendItemNo();
        lendItem.setLendItemNo(lendItemNo);        //投资编号
        lendItem.setLendId(investVO.getLendId());  //对应的标的id
        lendItem.setInvestUserId(investUserId);    //投资人id
        lendItem.setInvestName(investVO.getInvestName()); //投资人姓名
        lendItem.setInvestAmount(new BigDecimal(investVO.getInvestAmount()));
        lendItem.setLendYearRate(lend.getLendYearRate());
        lendItem.setInvestTime(LocalDateTime.now());
        lendItem.setLendStartDate(lend.getLendStartDate());
        lendItem.setLendEndDate(lend.getLendEndDate());

        //预期收益
        BigDecimal expectAmount = lendService.getInterestCount(
                lendItem.getInvestAmount(),
                lendItem.getLendYearRate(),
                lend.getPeriod(),
                lend.getReturnMethod());
        lendItem.setExpectAmount(expectAmount);

        //实际收益
        lendItem.setRealAmount(new BigDecimal("0"));
        //状态 默认为0
        lendItem.setStatus(0);
        baseMapper.insert(lendItem);

        //组装投资相关的参数, 提交到汇付宝资金托管平台**************
        //获取投资人的绑定协议号
        String votoBindCode = userBindService.getBindCodeByUserId(investUserId);
        //获取借款人的绑定协议号
        String benefitBindCode = userBindService.getBindCodeByUserId(lend.getUserId());

        //封装提交到汇付宝的参数
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("agentId", HfbConst.AGENT_ID);       //agent_id
        paramMap.put("voteBindCode", votoBindCode);       //vote_bind_code
        paramMap.put("benefitBindCode", benefitBindCode);       //benefit_bind_code
        paramMap.put("agentProjectCode", lend.getLendNo());       //agent_project_code
        paramMap.put("agentProjectName", lend.getTitle());       //agent_project_name
        paramMap.put("agentBillNo", lendItemNo);       //agent_bill_no
        paramMap.put("voteAmt", investVO.getInvestAmount());       //vote_amt
        paramMap.put("votePrizeAmt", "0");       //vote_prize_amt    投资奖励金额
        paramMap.put("voteFeeAmt", "0");       //vote_fee_amt    商户手续费
        paramMap.put("projectAmt", lend.getAmount());       //project_amt 项目总金额
        paramMap.put("note", "");       //note     投资备注
        paramMap.put("notifyUrl", HfbConst.INVEST_NOTIFY_URL);       //notify_url 接受投资信息完成后,同步返回商户的完整地址
        paramMap.put("returnUrl", HfbConst.INVEST_RETURN_URL);       //return_url
        paramMap.put("timestamp", RequestHelper.getTimestamp());       //timestamp
        String sign = RequestHelper.getSign(paramMap);
        paramMap.put("sign", sign);       //sign

        //构建充值自动提交表单
        String formStr = FormHelper.buildForm(HfbConst.INVEST_URL, paramMap);
        return formStr;

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void notify(Map<String, Object> paramMap) {
        System.out.println("投标成功!");

        //获取投资编号 商户订单号
        String agentBillNo = (String) paramMap.get("agentBillNo");
        boolean isSaved = transFlowService.isSaveTransFlow(agentBillNo);
        if (isSaved) {
            log.warn("幂等性返回");
            return;
        }
        //获取用户的绑定协议号和投资金额
        String bindCode = (String) paramMap.get("voteBindCode");
        String voteAmt = (String) paramMap.get("voteAmt");
        //修改商户系统中的用户账户金额:余额 冻结金额
        userAccountMapper.updateAccount(bindCode, new BigDecimal("-" + voteAmt), new BigDecimal(voteAmt));

        //修改投资记录的投资状态为已支付
        LendItem lendItem = this.getByLendItemNo(agentBillNo);
        lendItem.setStatus(1);//默认0  已支付1  已还款2
        baseMapper.updateById(lendItem);

        //修改标的信息: 投资人数 已投金额
        Long lendId = lendItem.getLendId();
        Lend lend = lendMapper.selectById(lendId);
        lend.setInvestNum(lend.getInvestNum() + 1);
        lend.setInvestAmount(lend.getInvestAmount().add(lendItem.getInvestAmount()));
        lendMapper.updateById(lend);

        //新增交易流水
        TransFlowBO transFlowBO = new TransFlowBO(agentBillNo, bindCode, new BigDecimal(voteAmt), TransTypeEnum.INVEST_LOCK, "投资项目编号：" + lend.getLendNo() + "，项目名称：" + lend.getTitle());
        transFlowService.saveTransFlow(transFlowBO);

    }

    @Override
    public List<LendItem> selectByLendId(Long lendId, Integer status) {
        QueryWrapper<LendItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("lend_id", lendId);
        List<LendItem> lendItemList = baseMapper.selectList(queryWrapper);
        return lendItemList;
    }

    //辅助方法
    private LendItem getByLendItemNo(String lendItemNo) {
        QueryWrapper<LendItem> queryWrapper = new QueryWrapper();
        queryWrapper.eq("lend_item_no", lendItemNo);
        return baseMapper.selectOne(queryWrapper);
    }
}
