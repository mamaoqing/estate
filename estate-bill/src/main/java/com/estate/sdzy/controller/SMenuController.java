package com.estate.sdzy.controller;


import com.estate.exception.BillException;
import com.estate.sdzy.entity.SMenu;
import com.estate.sdzy.service.SMenuService;
import com.estate.sdzy.service.SUserRoleService;
import com.estate.util.BillExceptionEnum;
import com.estate.util.MenuUtil;
import com.estate.util.Result;
import com.estate.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author mq
 * @since 2020-07-23
 */
@Controller
@RequestMapping("/sdzy/sMenu")
@Slf4j
public class SMenuController extends BaseController {

    @Autowired
    private SUserRoleService userRoleService;

    @Autowired
    private SMenuService sMenuService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/get")
    @ResponseBody
    public Result getMenuList(@RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(MenuUtil.getAllRoleMenu(sMenuService.listMenu(token)));
    }

    @PostMapping("/insertMenu")
    @ResponseBody
    public Result insertMenu(SMenu menu,@RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(sMenuService.insertMenu(menu, token));
    }


    @GetMapping("/{id}")
    @ResponseBody
    public Result getMenu(@PathVariable("id") long id) {
        return ResultUtil.success(sMenuService.getById(id));
    }

    @PutMapping("/updateMenu")
    @ResponseBody
    public Result updateMenu(SMenu menu,@RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(sMenuService.updateMenu(menu, token));
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public Result deleteMenu(@PathVariable("id") Long id,@RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(sMenuService.deleteMenuById(id, token));
    }

    @GetMapping("/textIndex")
    public Result testIndex() {

        System.out.println(1234);
        if (true) {
            throw new BillException(1, "测试成功了没有！！");
        }

        return ResultUtil.success();
    }

    @GetMapping("/bill")
    public Result billTestIndex() {

        System.out.println(1234);
        if (true) {
            throw new BillException(1, "测试成功了没有！！");
        }

        return ResultUtil.success();
    }
}

