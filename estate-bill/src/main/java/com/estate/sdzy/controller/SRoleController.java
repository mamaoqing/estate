package com.estate.sdzy.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.estate.exception.BillException;
import com.estate.sdzy.entity.SRole;
import com.estate.sdzy.service.SRoleService;
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
 * 角色表  前端控制器
 * </p>
 *
 * @author mq
 * @since 2020-07-23
 */
@Controller
@RequestMapping("/sdzy/sRole")
@Slf4j
public class SRoleController {

    @Autowired
    private SRoleService roleService;

    @GetMapping("/get")
    @ResponseBody
    public Result listRole(Integer pageNo, Integer size) {
//        List<SRole> list = roleService.list(null);
        try{
            if (StringUtils.isEmpty(pageNo)) {
                throw  new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
            }
            if (StringUtils.isEmpty(size)) {
                size = 10;
            }

            Page<SRole> page = new Page<>(pageNo, size);
            Page<SRole> pageList = roleService.page(page);
            return ResultUtil.success(pageList);
        }catch (Exception e){
            e.printStackTrace();
        }
        throw new BillException(BillExceptionEnum.SYSTEM_SELECT_ERROR);
    }

    @GetMapping("/{id}")
    public Result getRole(@PathVariable("id") Long id) {
        try {
            SRole role = roleService.getById(id);
            return ResultUtil.success(role);
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw  new BillException(BillExceptionEnum.SYSTEM_SELECT_ERROR);
    }

    @PutMapping("/updateRole")
    @ResponseBody
    public Result updateRole(SRole role, String token) {
        try {
            boolean flag = roleService.saveOrUpdate(role, token);
            return ResultUtil.success(flag);

        } catch (Exception e) {
            log.error("更新角色失败,失败信息：{}", e);
            e.printStackTrace();
        }
        throw new BillException(BillExceptionEnum.SYSTEM_UPDATE_ERROR);
    }

    @PostMapping("/insertRole")
    @ResponseBody
    public Result insertRole(SRole role) {
        try {
            boolean flag = roleService.save(role);
            if (flag) {
                log.info("添加角色成功");
                return ResultUtil.success(flag);
            }
        } catch (Exception e) {
            log.error("添加角色失败,失败信息：{}", e);
            e.printStackTrace();
        }
        throw new BillException(BillExceptionEnum.SYSTEM_INSERT_ERROR);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public Result deleteRole(@PathVariable("id") Long id) {
        try {
            boolean flag = roleService.removeById(id);
            if (flag) {
                log.info("删除角色成功,删除id：{}", id);
                return ResultUtil.success(flag);
            }
        } catch (Exception e) {
            log.error("删除角色失败,失败信息：{}", e);
            e.printStackTrace();
        }
        throw new BillException(BillExceptionEnum.SYSTEM_DELETE_ERROR);
    }

}

