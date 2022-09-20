package com.atguigu.srb.common.exception;

import com.atguigu.srb.common.result.ResponseEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * projectName: srb
 *
 * @author: GOD伟
 * time: 2022/6/13 13:52 周一
 * description: 断言类 封装所有业务判断
 */
@Slf4j
public abstract class Assert {
    /*
     * 断言对象不为空
     * 如果对象obj为空
     * */
    public static void notNull(Object obj, ResponseEnum responseEnum) {
        if (obj == null) {
            log.info("obj is null.................");
            throw new BusinessException(responseEnum);
        }
    }

    /*
     * 断言对象为空
     * 如果对象obj不为空,则抛出异常
     */
    public static void isNull(Object object, ResponseEnum responseEnum) {
        if (object != null) {
            log.info("obj is not null........");
            throw new BusinessException(responseEnum);
        }
    }

    /*
     * 断言表达式为真
     * 如果不为真,则抛出异常
     */
    public static void isTrue(boolean expression, ResponseEnum responseEnum) {
        if (!expression) {
            log.info("fail................");
            throw new BusinessException(responseEnum);
        }
    }

    /*断言两个对象不相等*/
    public static void notEquals(Object m1, Object m2, ResponseEnum responseEnum) {
        if (m1.equals(m2)) {
            log.info("equals..........");
            throw new BusinessException(responseEnum);
        }
    }

    /*断言两个对象相等*/
    public static void equals(Object m1, Object m2, ResponseEnum responseEnum) {
        if (!m1.equals(m2)) {
            log.info("not equals..........");
            throw new BusinessException(responseEnum);
        }
    }

    /*断言参数不为空*/
    public static void notEmpty(String s, ResponseEnum responseEnum) {
        if (StringUtils.isEmpty(s)) {
            log.info("arg is empty");
            throw new BusinessException(responseEnum);
        }
    }


}
