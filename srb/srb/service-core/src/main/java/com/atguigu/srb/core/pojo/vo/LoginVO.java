package com.atguigu.srb.core.pojo.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * projectName: srb
 *
 * @author: GOD伟
 * time: 2022/6/22 20:05 周三
 * description:
 */
@Data
@ApiModel
public class LoginVO {

    @ApiModelProperty(value = "用户类型")
    private Integer userType;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "密码")
    private String password;
}
