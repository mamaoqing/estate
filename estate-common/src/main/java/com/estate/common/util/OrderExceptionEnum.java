package com.estate.common.util;

/**
 * @author mq
 * @date 2020/7/30 14:46
 * @description 异常类的枚举
 */
public enum OrderExceptionEnum {
    /**
     *
     */
    SYSTEM_INSERT_ERROR ("添加数据系统异常",501),
    SYSTEM_UPDATE_ERROR ("修改数据系统异常",502),
    SYSTEM_DELETE_ERROR ("删除数据系统异常",503),
    SYSTEM_SELECT_ERROR ("查询数据系统异常",504),
    LOGIN_TIME_OUT ("用户登录超时",414),
    PARAMS_MISS_ERROR ("参数错误",415),
    PAGENO_MISS_ERROR ("参数错误,页码必传",415),
    SET_USER_ROLE_ERROR ("设置用户权限异常",416),
    SET_USER_COMM_ERROR ("设置用户数据权限异常",417),
    SET_ROLE_MENU_ERROR ("设置角色菜单异常",418),
    FILE_NOTFOUND_ERROR ("文件找不到",450),
    FILE_MASTER_FIELDNO_ERROR ("必填属性不能为空",450),
    FILE_TYPE_ERROR ("文件类型不正确",451),
    DATA_EXIST_ERROR ("数据重复",452),
    RESET_PASSWORD_ERROR ("旧密码输入错误，重设密码失败",900),
    RESET_PASSWORD_ERROR_SYSTEM ("重设密码失败",900),
    TREE_MENU_ERROR_SYSTEM ("社区树获取信息异常，请检查数据是否正确！",601),
    USER_ROLE_OUT_TIME ("用户角色权限已经过期！",1001),
    INSERT_BILL_CREATE_DATE_ERROR ("自动生成账单日期错误",700),
    ;
    private Integer code;

    private String msg;
    OrderExceptionEnum(String msg, Integer code){
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
