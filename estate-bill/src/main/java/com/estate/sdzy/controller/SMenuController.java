package com.estate.sdzy.controller;


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
public class SMenuController {

    @Autowired
    private SUserRoleService userRoleService;

    @Autowired
    private SMenuService sMenuService;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @GetMapping("/get")
    @ResponseBody
    public Result getMenuList(String token) {

        redisTemplate.opsForValue().set("a","123");

        List<Long> longs = userRoleService.listUserRole(token);

        List<SMenu> sMenus = sMenuService.listMenu(longs);

        System.out.println(redisTemplate.opsForValue().get("a")+"======================");

        List<SMenu> allRoleMenu = MenuUtil.getAllRoleMenu(sMenus);

        return ResultUtil.success(allRoleMenu);
    }

    @PostMapping("/insertMenu")
    @ResponseBody
    public Result insertMenu(SMenu menu) {
        try {
            boolean flag = sMenuService.insertMenu(menu);
            if (flag) {
                log.info("添加菜单成功");
                return ResultUtil.success();
            }
        } catch (Exception e) {
            log.error("添加菜单失败，失败信息：{}",e);
            e.printStackTrace();
        }
        return ResultUtil.error("菜单添加失败", 1);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Result getMenu(@PathVariable("id") long id) {
        try{
            SMenu menu = sMenuService.getById(id);
            return ResultUtil.success(menu);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResultUtil.error("查询菜单错误",1);
    }

    @PutMapping("/updateMenu")
    @ResponseBody
    public Result updateMenu(SMenu menu) {
        try {
            boolean flag = sMenuService.updateMenu(menu);
            if (flag) {
                log.info("菜单修改成功");
                return ResultUtil.success();
            }
        } catch (Exception e) {
            log.error("修改菜单失败，失败信息：{}",e);
            e.printStackTrace();
        }
        return ResultUtil.error("菜单修改失败", 1);
    }
    @DeleteMapping("/{id}")
    @ResponseBody
    public Result deleteMenu(@PathVariable("id") Long id) {
        try {
            boolean flag = sMenuService.deleteMenuById(id);
            if (flag) {
                log.info("菜单删除成功");
                return ResultUtil.success();
            }
        } catch (Exception e) {
            log.error("删除菜单失败，失败信息：{}",e);
            e.printStackTrace();
        }
        return ResultUtil.error("菜单删除失败", 1);
    }
}

