package com.estate.sdzy.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.estate.exception.BillException;
import com.estate.sdzy.entity.SUser;
import com.estate.sdzy.service.SUserService;
import com.estate.util.BillExceptionEnum;
import com.estate.util.Result;
import com.estate.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;

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
public class SUserController {

    @Autowired
    private SUserService userService;


    @GetMapping("/{id}")
    @ResponseBody
    public Result getUser(@PathVariable("id") Integer id) {
        return ResultUtil.success(userService.getById(id));
    }

    @GetMapping("/listUser")
    @ResponseBody
    public Result listUser(Integer pageNo, Integer size) {
        if (StringUtils.isEmpty(pageNo)) {
            throw new BillException(BillExceptionEnum.PAGENO_MISS_ERROR);
        }
        if (StringUtils.isEmpty(size)) {
            size = 10;
        }
        Page<SUser> page = new Page<>(pageNo, size);
        QueryWrapper<SUser> queryWrapper = new QueryWrapper<>();

        return ResultUtil.success(userService.page(page, queryWrapper));
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

    @PostMapping("/setUserRole")
    @ResponseBody
    public Result setUserRole(Long userId, String roleIds,@RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(userService.setUserRole(userId, roleIds, token));
    }

}

