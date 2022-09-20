package com.atguigu.srb.core.service;

import com.atguigu.srb.core.pojo.entity.BorrowInfo;
import com.atguigu.srb.core.pojo.vo.BorrowInfoApprovalVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 借款信息表 服务类
 * </p>
 *
 * @author God伟
 * @since 2022-06-12
 */
public interface BorrowInfoService extends IService<BorrowInfo> {


    Integer getBorrowInfoStatus(String userId);

    void saveBorrowInfo(BorrowInfo borrowInfo);

    List<BorrowInfo> selectAllList();

    Map<String, Object> getBorrowInfoDetail(Long borrowInfoId);

    void approval(BorrowInfoApprovalVO borrowInfoApprovalVO);

}
