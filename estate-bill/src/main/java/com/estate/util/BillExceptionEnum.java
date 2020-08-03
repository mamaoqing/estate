package com.estate.util;

import com.estate.exception.BillException;

/**
 * @author mq
 * @date 2020/7/30 14:46
 * @description 异常类的枚举
 */
public enum BillExceptionEnum {
    SYSTEM_INSERT_ERROR ("添加数据系统异常",501),
    SYSTEM_UPDATE_ERROR ("修改数据系统异常",502),
    SYSTEM_DELETE_ERROR ("删除数据系统异常",503),
    SYSTEM_SELECT_ERROR ("查询数据系统异常",504),
    LOGIN_TIME_OUT ("用户登录超时",414),
    PARAMS_MISS_ERROR ("参数错误",415),
    SET_USER_ROLE_ERROR ("设置用户权限异常",416),
    SET_ROLE_MENU_ERROR ("设置角色菜单异常",417),
    ;
    private Integer code;

    private String msg;
    BillExceptionEnum(String msg,Integer code){
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
