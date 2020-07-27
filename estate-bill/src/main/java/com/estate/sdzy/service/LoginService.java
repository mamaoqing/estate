package com.estate.sdzy.service;

import com.estate.util.Result;

import javax.servlet.http.HttpServletRequest;

public interface LoginService {
    Result login(HttpServletRequest req);
}
