package com.estate.sdzy.controller;


import com.estate.sdzy.service.SUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

/**
 * <p>
 * 用户角色 前端控制器
 * 用户登录之后，查询该用户对应的权限，
 * 然后根据用户的权限去查询对应的菜单
 * </p>
 *
 * @author mq
 * @since 2020-07-23
 */
@Controller
@RequestMapping("/sdzy/sUserRole")
public class SUserRoleController {

    @Autowired
    private SUserRoleService userRoleService;

    @GetMapping("{token}")
    public String getUserMenu(@PathVariable("token") String UUId){

        return "";
    }



}

