package com.estate.sdzy.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.estate.sdzy.entity.SUser;
import com.estate.sdzy.service.SUserService;
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
        List<SUser> one = userService.findOne(id);
        return ResultUtil.success(one);
    }

    @GetMapping("/listUser")
    @ResponseBody
    public Result listUser(Integer pageNo, Integer size) {
        if (StringUtils.isEmpty(pageNo)) {
            return ResultUtil.error("参数错误，请输入页码", 1);
        }
        if (StringUtils.isEmpty(size)) {
            size = 10;
        }
        try {
            Page<SUser> page = new Page<>(pageNo, size);
            IPage<SUser> userIPage = userService.page(page, null);
            return ResultUtil.success(userIPage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtil.error("查询用户出现错误", 1);
    }

    @PostMapping("/insertUser")
    @ResponseBody
    public Result insertUser(SUser user, String token) {
        try {
            boolean flag = userService.save(user,token);
            return ResultUtil.success(user);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtil.error("添加用户错误", 1);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public Result deleteUser(@PathVariable("id") Long id,String token) {
        try {
            boolean flag = userService.removeById(id,token);
            return ResultUtil.success(flag);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtil.error("删除用户错误", 1);
    }

    @PutMapping("/updateUser")
    @ResponseBody
    public Result updateUser(SUser user,String token) {
        try {
            boolean flag = userService.saveOrUpdate(user,token);
            return ResultUtil.success(flag);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtil.error("更新用户错误", 1);
    }

}

