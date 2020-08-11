package com.estate.sdzy.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.estate.sdzy.entity.SDict;
import com.estate.sdzy.service.SDictService;
import com.estate.sdzy.service.SUserService;
import com.estate.util.Result;
import com.estate.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 字典表 前端控制器
 * </p>
 *
 * @author mq
 * @since 2020-07-23
 */
@RestController
@RequestMapping("/sdzy/sDict")
@Slf4j
public class SDictController extends BaseController{

    @Autowired
    public SDictService sDictService;

    @Autowired
    private SUserService userService;

    @PostMapping("/insertDict")
    public Result insertDict(@RequestBody SDict sDict, @RequestHeader("Authentication-Token") String token) {
        boolean save = sDictService.save(sDict, token);
        if (save) {
            log.info("添加字典成功，公司id={}", sDict.getId());
            ResultUtil.success("添加字典成功");
        }
        return ResultUtil.error("添加字典失败！", 1);
    }

    @PutMapping("/updateDict")
    public Result updateDict(@RequestBody SDict sDict, @RequestHeader("Authentication-Token") String token) {
        //System.out.println(sDict);
        return ResultUtil.success(sDictService.update(sDict,token));
    }

    @GetMapping("/listDict")
    public Result listDict(Integer pageNo, Integer size, HttpServletRequest request) {
        return ResultUtil.success(sDictService.listDict(super.getParameterMap(request),pageNo,size));

    }

    @GetMapping("/listDictAll")
    public Result listDictAll(HttpServletRequest request) {
        QueryWrapper<SDict> queryWrapper = new QueryWrapper<>();
        //queryWrapper.eq("state","在用");
        queryWrapper.eq("is_delete","0");
        List<SDict> list = sDictService.list(queryWrapper);
        return ResultUtil.success(list);

    }

    @GetMapping("/{id}")
    public Result getDict(@PathVariable("id") Long id) {
        return ResultUtil.success(sDictService.getById(id));
    }

    @GetMapping("/checkDictName/{name}")
    public Result checkDictName(@PathVariable("name") String name,@RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(sDictService.checkDictName(name,token));//返回为true则表示重复
    }

    @DeleteMapping("/{id}")
    public Result deleteDict(@PathVariable("id") Long id, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(sDictService.removeById(id, token));
    }
}

