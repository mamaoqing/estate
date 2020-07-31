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
@RestController
@RequestMapping("/sdzy/sRole")
@Slf4j
public class SRoleController {

    @Autowired
    private SRoleService roleService;

    @GetMapping("/get")
    public Result listRole(Integer pageNo, Integer size) {
//        List<SRole> list = roleService.list(null);
        if (StringUtils.isEmpty(pageNo)) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        if (StringUtils.isEmpty(size)) {
            size = 10;
        }
        Page<SRole> page = new Page<>(pageNo, size);
        Page<SRole> pageList = roleService.page(page);
        return ResultUtil.success(pageList);
    }

    @GetMapping("/{id}")
    public Result getRole(@PathVariable("id") Long id) {
        return ResultUtil.success(roleService.getById(id));
    }

    @PutMapping("/updateRole")
    public Result updateRole(SRole role, String token) {
        return ResultUtil.success(roleService.saveOrUpdate(role, token));

    }

    @PostMapping("/insertRole")
    public Result insertRole(SRole role) {
        return ResultUtil.success(roleService.save(role));
    }

    @DeleteMapping("/{id}")
    public Result deleteRole(@PathVariable("id") Long id,String token) {
        return ResultUtil.success(roleService.remove(id,token));
    }
}

