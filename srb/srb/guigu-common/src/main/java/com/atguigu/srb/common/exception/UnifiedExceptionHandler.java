package com.atguigu.srb.common.exception;

import com.atguigu.srb.common.result.R;
import com.atguigu.srb.common.result.ResponseEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Component
@RestControllerAdvice  //在controller层添加通知. 如果使用@ControllerAdvice.则方法上需要添加@ResponseBody
public class UnifiedExceptionHandler {
    /*未定义异常*/
    @ExceptionHandler(value = Exception.class) //当controller中抛出Exception 就捕获该类异常
    public R handleException(Exception e) {
        log.error(e.getMessage(), e);
        return R.error();
    }

    /*特定异常*/
    @ExceptionHandler(value = BadSqlGrammarException.class)
    public R handleBadSqlGrammarException(BadSqlGrammarException e) {
        log.error(e.getMessage(), e);
        return R.setResult(ResponseEnum.BAD_SQL_GRAMMAR_ERROR);
    }

    /*自定义异常*/
    @ExceptionHandler(value = BusinessException.class)
    public R handleBusinessException(BusinessException e) {
        log.error(e.getMessage(), e);
        return R.error().message(e.getMessage()).code(e.getCode());
    }

}