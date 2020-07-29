package com.estate.exception;

import lombok.Data;

/**
 * @author mq
 * @date 2020/7/29 14:08
 * @description 自定义异常类
 */
public class BillException extends RuntimeException{

    private Integer code;

    public BillException(Integer code,String msg){

        super(msg);
        this.code = code;
    }

    public Integer getCode(){
        return  code;
    }
}
