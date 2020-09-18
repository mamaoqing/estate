package com.estate.sdzy.tariff.controller;


import com.estate.common.controller.BaseController;
import com.estate.common.util.Result;
import com.estate.common.util.ResultUtil;
import com.estate.sdzy.tariff.service.FAccountCostItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 账户 前端控制器
 * </p>
 *
 * @author mzc
 * @since 2020-09-10
 */
@RestController
@RequestMapping("/order/fAccountCostItem")
public class FAccountCostItemController extends BaseController {

    @Autowired
    private FAccountCostItemService fAccountCostItemService;

    @DeleteMapping("/{id}")
    public Result deleteAccountCostItem(@PathVariable("id") Long id, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(fAccountCostItemService.deleteItem(id, token));
    }
}

