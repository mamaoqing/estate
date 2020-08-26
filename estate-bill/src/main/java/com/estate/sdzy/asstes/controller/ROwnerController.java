package com.estate.sdzy.asstes.controller;


import com.estate.sdzy.asstes.entity.ROwner;
import com.estate.sdzy.asstes.service.ROwnerService;
import com.estate.util.Result;
import com.estate.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 业主表 前端控制器
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
@RestController
@RequestMapping("/sdzy/rOwner")
public class ROwnerController {

    @Autowired
    private ROwnerService ownerService;

    @PostMapping("/getOwenerList")
    public List<ROwner> getOwenerList(@RequestBody Map<String, String> map, @RequestHeader("Authentication-Token") String token) {
        return ownerService.getOwenerList(map, token);
    }

    @PostMapping("/insert")
    public Result insert(@RequestBody ROwner owner, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(ownerService.insert(owner, token));
    }

    @PostMapping("/update")
    public Result update(@RequestBody ROwner owner, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(ownerService.update(owner, token));
    }

    @PostMapping("/getCount")
    public Result getCount(@RequestBody ROwner owner, @RequestHeader("Authentication-Token") String token) {
        ROwner count = ownerService.getCount(owner, token);
        if (count != null) {
            return ResultUtil.success(count);
        } else {
            return ResultUtil.error("未找到相关信息", 1);
        }
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(ownerService.delete(id, token));
    }
}

