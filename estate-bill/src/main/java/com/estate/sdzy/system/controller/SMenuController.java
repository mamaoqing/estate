package com.estate.sdzy.system.controller;


import com.estate.common.controller.BaseController;
import com.estate.common.exception.BillException;
import com.estate.common.util.Result;
import com.estate.common.util.ResultUtil;
import com.estate.sdzy.system.entity.SMenu;
import com.estate.sdzy.system.service.SMenuService;
import com.estate.sdzy.system.service.SUserRoleService;
import com.estate.util.MenuUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author mq
 * @since 2020-07-23
 */
@RestController
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
    @GetMapping("/getMenuListUser")
    public  Result getMenuListUser(@RequestHeader("Authentication-Token") String token,HttpServletRequest request){
        return ResultUtil.success(sMenuService.getMenuListUser(token,super.getParameterMap(request)));
    }

    @PostMapping("/insertMenu")
    @ResponseBody
    public Result insertMenu(@RequestBody SMenu menu,@RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(sMenuService.insertMenu(menu, token));
    }


    @GetMapping("/{id}")
    @ResponseBody
    public Result getMenu(@PathVariable("id") long id) {
        return ResultUtil.success(sMenuService.getById(id));
    }

    @PutMapping("/updateMenu")
    @ResponseBody
    public Result updateMenu(@RequestBody SMenu menu,@RequestHeader("Authentication-Token") String token) {
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

