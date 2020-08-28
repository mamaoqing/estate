package com.estate.sdzy.system.controller;


import com.estate.common.util.Result;
import com.estate.common.util.ResultUtil;
import com.estate.sdzy.system.service.SUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RestController
@RequestMapping("/sdzy/sUserRole")
public class SUserRoleController {

    @Autowired
    private SUserRoleService userRoleService;

    @GetMapping("/listUserRole")
    public Result listUserRole(Long id, Long compId){
        return ResultUtil.success(userRoleService.listUserRole(id, compId));
    }




}

