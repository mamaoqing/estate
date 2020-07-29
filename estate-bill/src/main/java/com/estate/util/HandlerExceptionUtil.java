package com.estate.util;

import com.estate.exception.BillException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author mq
 * @date 2020/7/29 14:07
 * @description 捕获异常
 */
@Slf4j
@ControllerAdvice
public class HandlerExceptionUtil {

    @ExceptionHandler(BillException.class)
    @ResponseBody
    public Result handlerException(BillException billException){

        log.error(billException.getMessage());

        return ResultUtil.error(billException.getMessage(),billException.getCode());
    }
}
