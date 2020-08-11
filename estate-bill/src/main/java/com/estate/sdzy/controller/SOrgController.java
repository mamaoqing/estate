package com.estate.sdzy.controller;


import com.estate.sdzy.entity.SOrg;
import com.estate.sdzy.service.SOrgService;
import com.estate.util.Result;
import com.estate.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

/**
 * <p>
 * 机构表 前端控制器
 * </p>
 *
 * @author mq
 * @since 2020-07-23
 */
@RestController
@RequestMapping("/sdzy/sOrg")
public class SOrgController {

    @Autowired
    private SOrgService orgService;


    /**
     * 组织机构
     * 通过token获取用户，和公司信息
     * 一个机构对应多个下级机构
     * 最外层机构存在多个
     * 通过最外层的机构信息，递归查询所有的下级机构，直到没有下级
     * 返回一个最外层机构的集合
     */
    @GetMapping("/listOrg")
    public Result listOrg(@RequestHeader("Authentication-Token") String token){
        return ResultUtil.success(orgService.listOrg(token));
    }

    @GetMapping("/{id}")
    public Result getOrg(@PathVariable("id") Long id){
        return ResultUtil.success(orgService.getById(id));
    }

    @GetMapping("/getChildOrg")
    public Result getChildOrg(Long id){
        return ResultUtil.success(orgService.getById(id));
    }

    @PostMapping("/insertOrg")
    public Result insertOrg(@RequestHeader("Authentication-Token") String token,@RequestBody SOrg org){
        return ResultUtil.success(orgService.save(org,token));
    }

    @PutMapping("/updateOrg")
    public Result updateOrg(@RequestHeader("Authentication-Token") String token){
        return ResultUtil.success(orgService.listOrg(token));
    }
    @DeleteMapping("/{id}")
    public Result deleteOrg(@PathVariable("id") Long id,@RequestHeader("Authentication-Token") String token){
        return ResultUtil.success(orgService.removeById(id,token));
    }

    @GetMapping("/getBaseOrg")
    public Result getBaseOrg(Long compId){
        return ResultUtil.success(orgService.getBaseOrg(compId));
    }

    @GetMapping("/getOnlyChildOrg")
    public Result getOnlyChildOrg(Long id){
        return ResultUtil.success(orgService.getOnlyChildOrg(id));
    }


}

