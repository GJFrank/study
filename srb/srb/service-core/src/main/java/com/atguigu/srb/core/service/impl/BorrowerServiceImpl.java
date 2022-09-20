package com.atguigu.srb.core.service.impl;

import com.atguigu.srb.core.enums.BorrowerStatusEnum;
import com.atguigu.srb.core.enums.IntegralEnum;
import com.atguigu.srb.core.mapper.BorrowerAttachMapper;
import com.atguigu.srb.core.mapper.UserInfoMapper;
import com.atguigu.srb.core.mapper.UserIntegralMapper;
import com.atguigu.srb.core.pojo.entity.Borrower;
import com.atguigu.srb.core.mapper.BorrowerMapper;
import com.atguigu.srb.core.pojo.entity.BorrowerAttach;
import com.atguigu.srb.core.pojo.entity.UserInfo;
import com.atguigu.srb.core.pojo.entity.UserIntegral;
import com.atguigu.srb.core.pojo.vo.BorrowerApprovalVO;
import com.atguigu.srb.core.pojo.vo.BorrowerAttachVO;
import com.atguigu.srb.core.pojo.vo.BorrowerDetailVO;
import com.atguigu.srb.core.pojo.vo.BorrowerVO;
import com.atguigu.srb.core.service.BorrowerAttachService;
import com.atguigu.srb.core.service.BorrowerService;
import com.atguigu.srb.core.service.DictService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 借款人 服务实现类
 * </p>
 *
 * @author God伟
 * @since 2022-06-12
 */
@Service
public class BorrowerServiceImpl extends ServiceImpl<BorrowerMapper, Borrower> implements BorrowerService {


    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    BorrowerAttachMapper borrowerAttachMapper;

    @Autowired
    DictService dictService;
    @Autowired
    BorrowerAttachService borrowerAttachService;

    @Autowired
    UserIntegralMapper userIntegralMapper;

    @Override
    public Integer getBorrowerStatus(String userId) {
        QueryWrapper<Borrower> borrowerQueryWrapper = new QueryWrapper<>();
        borrowerQueryWrapper.eq("user_id", userId);
        Borrower borrower = baseMapper.selectOne(borrowerQueryWrapper);
        if (null == borrower) {
            return 0;
        }
        return borrower.getStatus();
    }

    @Override
    public void saveBorrower(BorrowerVO borrowerVO, String userId) {
        //保存borrower
        Borrower borrower = new Borrower();

        BeanUtils.copyProperties(borrowerVO, borrower);
        borrower.setUserId(Long.parseLong(userId));

        UserInfo userInfo = userInfoMapper.selectById(userId);
        borrower.setName(userInfo.getName());
        borrower.setIdCard(userInfo.getIdCard());
        borrower.setMobile(userInfo.getMobile());

        //是否结婚 是 1  否 0
        borrower.setIsMarry(borrowerVO.getMarry() ? 1 : 0);
        //borrower status
        borrower.setStatus(BorrowerStatusEnum.AUTH_RUN.getStatus());

        baseMapper.insert(borrower);


        //保存borrower_attach
        Long borrowerId = borrower.getId();
        for (BorrowerAttach borrowerAttach : borrowerVO.getBorrowerAttachList()) {
            borrowerAttach.setBorrowerId(borrowerId);
            borrowerAttachMapper.insert(borrowerAttach);
        }
    }

    @Override
    public BorrowerDetailVO getBorrowerDetailVOById(Long id) {
        Borrower borrower = baseMapper.selectById(id);

        BorrowerDetailVO borrowerDetailVO = new BorrowerDetailVO();
        BeanUtils.copyProperties(borrower, borrowerDetailVO);

        //婚否
        borrowerDetailVO.setMarry(borrower.getIsMarry() == 1 ? "是" : "否");
        //性别
        borrowerDetailVO.setSex(borrower.getSex() == 1 ? "男" : "女");

        //计算下拉列表选中内容
        String education = dictService.getNameByParentDictCodeAndValue("education", borrower.getEducation());
        String industry = dictService.getNameByParentDictCodeAndValue("industry", borrower.getIndustry());
        String income = dictService.getNameByParentDictCodeAndValue("income", borrower.getIncome());
        String returnSource = dictService.getNameByParentDictCodeAndValue("returnSource", borrower.getReturnSource());
        String contactsRelation = dictService.getNameByParentDictCodeAndValue("relation", borrower.getContactsRelation());

        //设置下拉列表选中内容
        borrowerDetailVO.setEducation(education);
        borrowerDetailVO.setIndustry(industry);
        borrowerDetailVO.setIncome(income);
        borrowerDetailVO.setReturnSource(returnSource);
        borrowerDetailVO.setContactsRelation(contactsRelation);

        //审批状态
        String status = BorrowerStatusEnum.getMsgByStatus(borrower.getStatus());
        borrowerDetailVO.setStatus(status);

        //获取附件VO列表
        List<BorrowerAttachVO> borrowerAttachVOList = borrowerAttachService.selectBorrowerAttachVOList(id);
        borrowerDetailVO.setBorrowerAttachVOList(borrowerAttachVOList);

        return borrowerDetailVO;
    }

    @Override
    public IPage<Borrower> listPage(Page<Borrower> pageParam, String keyword) {
        QueryWrapper<Borrower> borrowerQueryWrapper = new QueryWrapper<>();

        if (StringUtils.isEmpty(keyword)) {
            return baseMapper.selectPage(pageParam, null);
        }
        borrowerQueryWrapper.like("name", keyword)
                .or().like("id_card", keyword)
                .or().like("mobile", keyword)
                .orderByDesc("id");

        return baseMapper.selectPage(pageParam, borrowerQueryWrapper);
    }

    @Override
    public void approval(BorrowerApprovalVO borrowerApprovalVO) {

        //借款人认证状态
        Long borrowerId = borrowerApprovalVO.getBorrowerId();
        Borrower borrower = baseMapper.selectById(borrowerId);
        //将审核结果带给borrower
        borrower.setStatus(borrowerApprovalVO.getStatus());
        baseMapper.updateById(borrower);

        Long userId = borrower.getUserId();
        UserInfo userInfo = userInfoMapper.selectById(userId);

        //添加积分
        UserIntegral userIntegral = new UserIntegral();
        userIntegral.setUserId(userId);
        //前端进行的计算?没有  //基本信息积分 approvalForm.infoIntegral
        userIntegral.setIntegral(borrowerApprovalVO.getInfoIntegral());
        userIntegral.setContent("借款人基本信息");
        userIntegralMapper.insert(userIntegral);

        //进行加分项判断
        int curIntegral =  borrowerApprovalVO.getInfoIntegral();
        //用户身份证积分
        if (borrowerApprovalVO.getIsIdCardOk()) {
            curIntegral += IntegralEnum.BORROWER_IDCARD.getIntegral();
            UserIntegral userIdCardIntegral = new UserIntegral();
            userIdCardIntegral.setUserId(userId);
            userIdCardIntegral.setIntegral(IntegralEnum.BORROWER_IDCARD.getIntegral());
            userIdCardIntegral.setContent(IntegralEnum.BORROWER_IDCARD.getMsg());
            userIntegralMapper.insert(userIdCardIntegral);
        }
        //用户车子积分
        if (borrowerApprovalVO.getIsCarOk()) {
            curIntegral += IntegralEnum.BORROWER_CAR.getIntegral();
            UserIntegral userCarIntegral = new UserIntegral();
            userCarIntegral.setUserId(userId);
            userCarIntegral.setIntegral(IntegralEnum.BORROWER_CAR.getIntegral());
            userCarIntegral.setContent(IntegralEnum.BORROWER_CAR.getMsg());
            userIntegralMapper.insert(userCarIntegral);
        }
        //用户房产积分
        if (borrowerApprovalVO.getIsHouseOk()) {
            curIntegral += IntegralEnum.BORROWER_HOUSE.getIntegral();
            UserIntegral userHouseIntegral = new UserIntegral();
            userHouseIntegral.setUserId(userId);
            userHouseIntegral.setIntegral(IntegralEnum.BORROWER_HOUSE.getIntegral());
            userHouseIntegral.setContent(IntegralEnum.BORROWER_HOUSE.getMsg());
            userIntegralMapper.insert(userHouseIntegral);
        }
        //更新最新的用户积分
        userInfo.setIntegral(curIntegral);

        //修改审核状态  借款人认证状态
        userInfo.setBorrowAuthStatus(borrowerApprovalVO.getStatus());
        userInfoMapper.updateById(userInfo);


    }
}
