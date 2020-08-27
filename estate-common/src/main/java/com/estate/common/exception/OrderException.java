package com.estate.common.exception;

import com.estate.common.util.OrderExceptionEnum;

/**
 * @author mq
 * @date 2020/7/29 14:08
 * @description 自定义异常类
 */
public class OrderException extends RuntimeException{

    private Integer code;

    public OrderException(OrderExceptionEnum exceptionEnum){

        super(exceptionEnum.getMsg());
        this.code = exceptionEnum.getCode();
    }

    public OrderException(Integer code, String msg){

        super(msg);
        this.code = code;
    }


    public Integer getCode(){
        return  code;
    }
}
