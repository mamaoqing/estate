package com.estate.sdzy.controller;


import com.estate.exception.BillException;
import com.estate.sdzy.entity.SMenu;
import com.estate.sdzy.service.SMenuService;
import com.estate.sdzy.service.SUserRoleService;
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
    public Result getMenuList(String token) {
        try {
            List<Long> longs = userRoleService.listUserRole(token);
            List<SMenu> sMenus = sMenuService.listMenu(longs);
            List<SMenu> allRoleMenu = MenuUtil.getAllRoleMenu(sMenus);

            return ResultUtil.success(allRoleMenu);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtil.error("菜单查询失败，请稍后再试", 1);
    }

    @PostMapping("/insertMenu")
    @ResponseBody
    public Result insertMenu(SMenu menu, String token) {
        try {
            boolean flag = sMenuService.insertMenu(menu, token);
            return ResultUtil.success(flag);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtil.error("菜单添加失败", 1);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Result getMenu(@PathVariable("id") long id) {
        try {
            SMenu menu = sMenuService.getById(id);
            return ResultUtil.success(menu);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtil.error("查询菜单错误", 1);
    }

    @PutMapping("/updateMenu")
    @ResponseBody
    public Result updateMenu(SMenu menu, String token) {
        try {
            boolean flag = sMenuService.updateMenu(menu, token);
            return ResultUtil.success(flag);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtil.error("系统错误，菜单修改失败。", 1);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public Result deleteMenu(@PathVariable("id") Long id, String token) {
        try {
            boolean flag = sMenuService.deleteMenuById(id, token);
            return ResultUtil.success(flag);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtil.error("菜单删除失败", 1);
    }

    @GetMapping("/textIndex")
    public Result testIndex(){

        System.out.println(1234);
        if(true){
            throw  new BillException(1,"测试成功了没有！！");
        }

        return ResultUtil.success();
    }

    @GetMapping("/bill")
    public Result billTestIndex(){

        System.out.println(1234);
        if(true){
            throw  new BillException(1,"测试成功了没有！！");
        }

        return ResultUtil.success();
    }
}

