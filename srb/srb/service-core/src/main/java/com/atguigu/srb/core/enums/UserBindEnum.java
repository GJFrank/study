package com.atguigu.srb.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * projectName: gitclone
 *
 * @author: GOD伟
 * time: 2022/6/29 12:59 周三
 * description:
 */
@Getter
@AllArgsConstructor
public enum UserBindEnum {
    NO_BIND(0, "未绑定"),
    BIND_OK(1, "绑定成功"),
    BIND_FAIL(-1, "绑定失败"),
    ;

    private Integer status;
    private String msg;
}
