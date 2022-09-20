package com.atguigu.srb.core.service;

import com.atguigu.srb.core.pojo.entity.Borrower;
import com.atguigu.srb.core.pojo.vo.BorrowerApprovalVO;
import com.atguigu.srb.core.pojo.vo.BorrowerDetailVO;
import com.atguigu.srb.core.pojo.vo.BorrowerVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 借款人 服务类
 * </p>
 *
 * @author God伟
 * @since 2022-06-12
 */
public interface BorrowerService extends IService<Borrower> {

    Integer getBorrowerStatus(String userId);

    void saveBorrower(BorrowerVO borrowerVO, String userId);

    BorrowerDetailVO getBorrowerDetailVOById(Long id);

    IPage<Borrower> listPage(Page<Borrower> pageParam, String keyword);

    void approval(BorrowerApprovalVO borrowerApprovalVO);
}
