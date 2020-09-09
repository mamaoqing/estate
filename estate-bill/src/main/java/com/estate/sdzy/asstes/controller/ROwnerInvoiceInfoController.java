package com.estate.sdzy.asstes.controller;


import com.estate.common.util.Result;
import com.estate.common.util.ResultUtil;
import com.estate.sdzy.asstes.entity.ROwnerInvoiceInfo;
import com.estate.sdzy.asstes.service.ROwnerInvoiceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 业主开票信息 前端控制器
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
@RestController
@RequestMapping("/sdzy/rOwnerInvoiceInfo")
public class ROwnerInvoiceInfoController {

    @Autowired
    public ROwnerInvoiceInfoService ownerInvoiceInfoService;

    @PostMapping("/getList")
    public Result getListByOwnerId(@RequestBody Map<String, Long> map, @RequestHeader("Authentication-Token") String token) {
        Map data = new HashMap();
        if (!StringUtils.isEmpty(map.get("pageNo")) && !StringUtils.isEmpty(map.get("size"))) {
            Long pageNum = (map.get("pageNo") - 1) * map.get("size");
            map.put("pageNo", pageNum);
        }
        data.put("data", ownerInvoiceInfoService.getList(map, token));
        data.put("pageTotal", ownerInvoiceInfoService.getPageTotal(map, token));
        return ResultUtil.success(data);
    }

    @PostMapping("/add")
    public Result add(@RequestBody ROwnerInvoiceInfo ownerInvoiceInfo, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(ownerInvoiceInfoService.add(ownerInvoiceInfo, token));
    }

    @PostMapping("/update")
    public Result update(@RequestBody ROwnerInvoiceInfo ownerInvoiceInfo, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(ownerInvoiceInfoService.update(ownerInvoiceInfo, token));
    }

    @PostMapping("/getInfo")
    public Result getInfo(@RequestBody ROwnerInvoiceInfo ownerInvoiceInfo, @RequestHeader("Authentication-Token") String token) {
        ROwnerInvoiceInfo info = ownerInvoiceInfoService.getInfo(ownerInvoiceInfo, token);
        if (info != null) {
            return ResultUtil.success(info);
        } else {
            return ResultUtil.error("没有找打相同的信息", 1);
        }
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(ownerInvoiceInfoService.delete(id, token));
    }

}

