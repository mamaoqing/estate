package com.estate.sdzy.system.controller;


import com.estate.common.controller.BaseController;
import com.estate.common.util.Result;
import com.estate.common.util.ResultUtil;
import com.estate.sdzy.system.entity.SDictItem;
import com.estate.sdzy.system.service.SDictItemService;
import com.estate.sdzy.system.service.SUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * <p>
 * 字典表 前端控制器
 * </p>
 *
 * @author mq
 * @since 2020-07-23
 */
@RestController
@RequestMapping("/sdzy/sDictItem")
@Slf4j
public class SDictItemController extends BaseController {

    @Autowired
    public SDictItemService sDictItemService;

    @Autowired
    private SUserService userService;

    @PostMapping("/insertDictItem")
    public Result insertDictItem(@RequestBody SDictItem sDictItem, @RequestHeader("Authentication-Token") String token) {
        boolean save = sDictItemService.save(sDictItem, token);
        if (save) {
            log.info("添加字典成功，公司id={}", sDictItem.getId());
            ResultUtil.success("添加字典成功");
        }
        System.err.println(new Date());
        return ResultUtil.error("添加字典失败！", 1);
    }

    @PutMapping("/updateDictItem")
    public Result updateDictItem(@RequestBody SDictItem sDictItem, @RequestHeader("Authentication-Token") String token) {
        //System.out.println(sDict);
        return ResultUtil.success(sDictItemService.update(sDictItem,token));
    }

    @GetMapping("/listDictItem")
    public Result listDictItem(Integer pageNo, Integer size, HttpServletRequest request, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(sDictItemService.listDictItem(super.getParameterMap(request),pageNo,size,token));

    }

    @GetMapping("/findDictItemList")
    public Result findDictItemList(Integer pageNo, Integer size, HttpServletRequest request, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(sDictItemService.findDictItemList(super.getParameterMap(request),pageNo,size,token));

    }

    @GetMapping("/getDictItemByDictId/{dictId}")
    public Result getDictItemByDictId(@PathVariable("dictId") Long dictId) {
        return ResultUtil.success(sDictItemService.getDictItemByDictId(dictId));
    }

    @GetMapping("/{id}")
    public Result getDictItem(@PathVariable("id") Long id) {
        return ResultUtil.success(sDictItemService.getById(id));
    }

    @GetMapping("/{dictId}/{name}")
    public Result checkDictItemName(@PathVariable("dictId") String dictId,@PathVariable("name") String name,@RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(sDictItemService.checkDictItemName(dictId,name,token));//返回为true则表示重复
    }

    @DeleteMapping("/{id}")
    public Result deleteDictItem(@PathVariable("id") Long id, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(sDictItemService.removeById(id, token));
    }
}

