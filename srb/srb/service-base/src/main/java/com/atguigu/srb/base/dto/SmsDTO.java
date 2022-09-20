package com.atguigu.srb.base.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * projectName: gitclone
 *
 * @author: GOD伟
 * time: 2022/7/5 14:43 周二
 * description:
 */

@Data
@ApiModel(description = "短信")
public class SmsDTO {

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "消息内容")
    private String message;
}

