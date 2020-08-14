package com.estate.sdzy.system.controller;

import com.estate.sdzy.common.controller.BaseController;
import com.estate.sdzy.system.entity.SUnitModel;
import com.estate.sdzy.system.service.SUnitModelService;
import com.estate.util.Result;
import com.estate.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 单元型号表 前端控制器
 * @author mzc
 * @since 2020-08-11
 */
@RestController
@RequestMapping("/sdzy/sUnitModel")
//@Controller
public class SUintModelController extends BaseController {

    @Autowired
    private SUnitModelService sUnitModelService;

    @GetMapping("/listUnitModel")
    public Result listUnitModel(Integer pageNo, Integer size, HttpServletRequest request, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(sUnitModelService.listUnitModel(super.getParameterMap(request),pageNo,size,token));
    }

    @GetMapping("/{id}")
    public Result getUintModel(@PathVariable("id") Long id) {
        return ResultUtil.success(sUnitModelService.getById(id));
    }

    @PutMapping("/updateUnitModel")
    public Result updateUintModel(@RequestBody SUnitModel sUintModel, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(sUnitModelService.saveOrUpdate(sUintModel, token));

    }

    @PostMapping("/insertUnitModel")
    public Result insertUintModel(@RequestBody SUnitModel sUintModel, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(sUnitModelService.save(sUintModel,token));
    }

    @DeleteMapping("/{id}")
    public Result deleteUintModel(@PathVariable("id") Long id,@RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(sUnitModelService.remove(id,token));
    }
}
