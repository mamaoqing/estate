package com.estate.sdzy.tariff.controller;


import com.estate.common.controller.BaseController;
import com.estate.common.util.Result;
import com.estate.common.util.ResultUtil;
import com.estate.sdzy.tariff.service.FFinanceRecordService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 财务流水 前端控制器
 * </p>
 *
 * @author mq
 * @since 2020-09-08
 */
@RestController
@RequestMapping("/order/fFinanceRecord")
public class FFinanceRecordController extends BaseController {
    @Autowired
    public FFinanceRecordService fFinanceRecordService;

    @PostMapping("/pay")
    public Result pay(@RequestBody Map<String, String> map, @RequestHeader("Authentication-Token") String token) {
        System.out.println(map);
        return ResultUtil.success(fFinanceRecordService.insert(map, token));
    }

    @PostMapping("/payPrice")
    public Result payPrice(@RequestBody Map<String, String> map, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(fFinanceRecordService.payPrice(map, token));
    }

    @GetMapping("/getOwnerByName")
    public Result getOwnerByName(@Param("ownerName") String ownerName,@Param("tel") String tel, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(fFinanceRecordService.getOwnerByName(ownerName,tel, token));
    }

    @GetMapping("/getOwnerBill")
    public Result getOwnerBill(@Param("ownerId") Long ownerId, @Param("pageNo") Long pageNo, @Param("size") Long size, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(fFinanceRecordService.getOwnerBill(ownerId, pageNo, size, token));
    }

    @GetMapping("/listFinanceRecord")
    public Result listFinanceRecord(Integer pageNo, Integer size,HttpServletRequest request){
        return ResultUtil.success(fFinanceRecordService.getFinanceRecords(super.getParameterMap(request),pageNo,size));
    }
}

