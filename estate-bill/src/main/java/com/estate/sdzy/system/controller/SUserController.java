package com.estate.sdzy.system.controller;


import com.estate.sdzy.common.controller.BaseController;
import com.estate.sdzy.system.entity.SUser;
import com.estate.sdzy.system.service.SUserService;
import com.estate.util.Result;
import com.estate.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author mq
 * @since 2020-07-23
 */
@Controller
@RequestMapping("/sdzy/sUser")
@Slf4j
public class SUserController extends BaseController {

    @Autowired
    private SUserService userService;


    @GetMapping("/{id}")
    @ResponseBody
    public Result getUser(@PathVariable("id") Integer id) {
        return ResultUtil.success(userService.getById(id));
    }

    @GetMapping("/listUser")
    @ResponseBody
    public Result listUser(@RequestHeader("Authentication-Token") String token,HttpServletRequest request) {
        return ResultUtil.success(userService.listUser(token,super.getParameterMap(request)));
    }

    @PostMapping("/insertUser")
    @ResponseBody
    public Result insertUser(SUser user,@RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(userService.save(user, token));
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public Result deleteUser(@PathVariable("id") Long id,@RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(userService.removeById(id, token));
    }

    @PutMapping("/updateUser")
    @ResponseBody
    public Result updateUser(SUser user,@RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(userService.saveOrUpdate(user, token));
    }

    @PutMapping("/reSetPassword")
    @ResponseBody
    public Result reSetPassword(String oldPassword,String newPassword,Long id,@RequestHeader("Authentication-Token") String token){

        return ResultUtil.success(userService.reSetPassword(newPassword,id,token,oldPassword));
    }
    @PutMapping("/reSetPasswordAdmin")
    @ResponseBody
    public Result reSetPasswordAdmin(String password,Long id,@RequestHeader("Authentication-Token") String token){

        return ResultUtil.success(userService.reSetPassword(password,token,id));
    }

    @PostMapping("/setUserRole")
    @ResponseBody
    public Result setUserRole(Long userId,Long compId, String roleIds,@RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(userService.setUserRole(userId,compId, roleIds, token));
    }

}

