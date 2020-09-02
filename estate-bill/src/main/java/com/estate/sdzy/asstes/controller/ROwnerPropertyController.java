package com.estate.sdzy.asstes.controller;


import com.estate.common.util.Result;
import com.estate.common.util.ResultUtil;
import com.estate.sdzy.asstes.entity.ROwnerProperty;
import com.estate.sdzy.asstes.service.ROwnerPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 业主与物业对应关系 前端控制器
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
@RestController
@RequestMapping("/sdzy/rOwnerProperty")
public class ROwnerPropertyController {

    @Autowired
    private ROwnerPropertyService ownerPropertyService;

    @GetMapping("/ownerPro/{id}")
    public Result ownerPro(@PathVariable("id") Long id) {
        return ResultUtil.success(ownerPropertyService.ownerProByParkId(id));
    }

    @GetMapping("/getOwnerProp/{ownerId}")
    public Result getOwnerProperty(@PathVariable("ownerId") Long ownerId) {
        return ResultUtil.success(ownerPropertyService.getOwnerProperty(ownerId));
    }

    @PostMapping("/getAllProp")
    public Result getAllProp(@RequestBody Map map, @RequestHeader("Authentication-Token") String token) {
        Map data = new HashMap();
        if (!StringUtils.isEmpty(map.get("pageNo")) && !StringUtils.isEmpty(map.get("size"))) {
            Long pageNum = (Long.parseLong(map.get("pageNo").toString()) - 1) * Long.parseLong(map.get("size").toString());
            map.put("pageNo", pageNum);
        }
        data.put("data", ownerPropertyService.getAllProperty(map, token));
        data.put("pageTotal", ownerPropertyService.getPageTotal(map, token));
        return ResultUtil.success(data);
    }

    @DeleteMapping("/deleteOwnerProp/{id}")
    public Result deleteOwnerProp(@PathVariable("id") Long id, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(ownerPropertyService.delete(id, token));
    }

    @PostMapping("/update")
    public Result update(@RequestBody ROwnerProperty ownerProperty, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(ownerPropertyService.update(ownerProperty, token));
    }

    @PostMapping("/insertRoomOwnerOrPark")
    public Result getOwnerProperty(@RequestBody Map map, @RequestHeader("Authentication-Token") String token) {
        boolean b = ownerPropertyService.insertRoomOwnerOrPark(map, token);
        if (b) {
            return ResultUtil.success();
        } else {
            return ResultUtil.error("业主重复", 1);
        }
    }
}

