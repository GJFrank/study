package com.atguigu.srb.core.pojo.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * projectName: gitclone
 *
 * @author: GOD伟
 * time: 2022/7/6 11:17 周三
 * description: 投标信息
 */
@Data
@ApiModel(description = "投标信息")
public class InvestVO {

    private Long lendId;

    //投标金额
    private String investAmount;

    //用户名id
    private  Long investUserId;

    //用户姓名
    private String investName;
}
