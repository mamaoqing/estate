package com.estate.sdzy.tariff.controller;


import com.estate.common.controller.BaseController;
import com.estate.common.util.Result;
import com.estate.common.util.ResultUtil;
import com.estate.sdzy.tariff.entity.FAccount;
import com.estate.sdzy.tariff.service.FAccountService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 账户 前端控制器
 * </p>
 *
 * @author mzc
 * @since 2020-09-10
 */
@RestController
@RequestMapping("/order/fAccount")
public class FAccountController extends BaseController {

    @Resource(name="accountService")
    private FAccountService accountService;

    @GetMapping("/listAccount")
    public Result listAccountCostItem(Integer pageNo, Integer size,@RequestHeader("Authentication-Token") String token, HttpServletRequest request){
        return ResultUtil.success(accountService.listAccount(super.getParameterMap(request),pageNo,size,token));
    }

    @PostMapping("/insertAccount")
    public Result insertAccountCostItem(@RequestHeader("Authentication-Token") String token, @RequestBody FAccount account){
        return ResultUtil.success(accountService.save(account,token));
    }

    @PutMapping("/updateAccount")
    public Result updateAccountCostItem(@RequestHeader("Authentication-Token") String token, @RequestBody FAccount account){
        return ResultUtil.success(accountService.saveOrUpdate(account,token));
    }

    @PostMapping("/getAccount")
    public Result getAccount(Long ownerId,Long ruleId ){
        return ResultUtil.success(accountService.getAccount(ownerId,ruleId));
    }

    @GetMapping("/getAccountByOwnerId/{ownerId}")
    public Result getAccountByOwnerId(@PathVariable("ownerId") Long ownerId,@RequestHeader("Authentication-Token") String token){
        return ResultUtil.success(accountService.getAccountByOwnerId(ownerId,token));
    }

    @GetMapping("/getType")
    public Result getType(@RequestHeader("Authentication-Token") String token, HttpServletRequest request){
        return ResultUtil.success(accountService.listTypes(super.getParameterMap(request),token));
    }
}

