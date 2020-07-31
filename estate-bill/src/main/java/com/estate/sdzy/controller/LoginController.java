package com.estate.sdzy.controller;

import com.estate.sdzy.entity.SUser;
import com.estate.sdzy.service.LoginService;
import com.estate.sdzy.service.SUserService;
import com.estate.util.Result;
import com.estate.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * <p>
 * 登陆 前端控制器
 * </p>
 *
 * @author cfy
 * @since 2020-07-24
 */
@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * create by: cfy
     * description: TODO
     * create time: 2020/7/28 11:18
     * 
      * @Param: null
     * @return 
     */
    @RequestMapping(value = "/login")
    public Result login(HttpServletRequest req) {
        return loginService.login(req);
    }

    @GetMapping("/logout")
    public Result logout(String token){

        return ResultUtil.success(loginService.logout(token));
    }

}
