package com.atguigu.srb.core.service;

import com.atguigu.srb.core.pojo.entity.UserBind;
import com.atguigu.srb.core.pojo.vo.UserBindVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 用户绑定表 服务类
 * </p>
 *
 * @author God伟
 * @since 2022-06-12
 */
public interface UserBindService extends IService<UserBind> {

    String commitBind(UserBindVO userBindVO,String userId);

    void notifyBind(Map<String, Object> resultMap);

    String getBindCodeByUserId(Long investUserId);
}
