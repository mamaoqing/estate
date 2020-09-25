package com.estate.sdzy.tariff.controller;


import com.estate.common.util.Result;
import com.estate.common.util.ResultUtil;
import com.estate.sdzy.tariff.entity.FVoucher;
import com.estate.sdzy.tariff.service.FVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author mq
 * @since 2020-09-23
 */
@RestController
@RequestMapping("/order/fVoucher")
public class FVoucherController {

    @Autowired
    public FVoucherService voucherService;

    @GetMapping("getAll")
    public Result getAll(@RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(voucherService.getAll(token));
    }

    @PostMapping("getOwners")
    public Result getOwners(@RequestBody Map<String, String> map, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(voucherService.getOwners(token, map));
    }

    @PostMapping("insert")
    public Result insert(@RequestBody FVoucher voucher, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(voucherService.insert(voucher, token));
    }

    @GetMapping("getNo")
    public Result getNo( @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(voucherService.getNo(token));
    }

    @GetMapping("deleteVoucher/{id}")
    public Result deleteVoucher(@PathVariable("id") Long id ,@RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(voucherService.deleteVoucher(id,token));
    }
}

