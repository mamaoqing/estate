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
        if(StringUtils.isEmpty(pageNo)){
            return ResultUtil.error("参数错误，请输入页码",1);
        }
        if(StringUtils.isEmpty(size)){
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
    public Result insertUser(SUser user) {
        try {
            boolean flag = userService.save(user);
            if (flag) {
                log.info("添加用户信息成功：{}", user);
                return ResultUtil.success(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.error("添加用户信息失败：{}", user);
        return ResultUtil.error("添加用户错误", 1);
    }

    @DeleteMapping("/deleteUser")
    @ResponseBody
    public Result deleteUser(Long id) {
        try {
            boolean flag = userService.removeById(id);
            if (flag) {
                log.info("删除用户信息成功用户id：{}",id);
                return ResultUtil.success(flag);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.error("删除用户信息失败,用户id：{}", id);
        return ResultUtil.error("删除用户错误", 1);
    }

    @PutMapping("/updateUser")
    @ResponseBody
    public Result updateUser(SUser user) {
        try {
            boolean flag = userService.saveOrUpdate(user);
            if (flag) {
                log.info("更新用户信息成功用户id：{}",user);
                return ResultUtil.success(flag);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.error("更新用户信息失败,用户id：{}", user);
        return ResultUtil.error("更新用户错误", 1);
    }

}

