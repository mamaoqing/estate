package com.estate.sdzy.common.service;

import com.estate.common.util.Result;

import javax.servlet.http.HttpServletRequest;

public interface LoginService {
    Result login(HttpServletRequest req);

    /**
     * 退出方法
     * @param token  用户登录唯一凭证
     * @return
     */
    boolean logout(String token);
}
