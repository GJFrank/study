package com.atguigu.srb.core.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * projectName: gitclone
 *
 * @author: GOD伟
 * time: 2022/7/1 13:37 周五
 * description:
 */
@Data
@ApiModel(description = "借款人审批")
public class BorrowerApprovalVO {

    @ApiModelProperty(value = "id")
    private Long borrowerId;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "身份证信息是否正确")
    private Boolean isIdCardOk;

    @ApiModelProperty(value = "房产信息是否正确")
    private Boolean isHouseOk;

    @ApiModelProperty(value = "车辆信息是否正确")
    private Boolean isCarOk;

    @ApiModelProperty(value = "基本信息积分")
    private Integer infoIntegral;
}