package com.estate.sdzy.asstes.controller;


import com.estate.sdzy.asstes.entity.RCommRoleAgreement;
import com.estate.sdzy.asstes.service.RCommRoleAgreementService;
import com.estate.sdzy.common.controller.BaseController;
import com.estate.util.Result;
import com.estate.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author mq
 * @since 2020-07-28
 */
@RestController
@RequestMapping("/sdzy/rCommRoleAgreement")
public class RCommRoleAgreementController extends BaseController {

    @Autowired
    private RCommRoleAgreementService rCommRoleAgreementService;


    @GetMapping("/listAgreement")
    public Result listAgreement(@RequestHeader("Authentication-Token") String token, HttpServletRequest request) {
        return ResultUtil.success(rCommRoleAgreementService.listCommRoleAgreement(super.getParameterMap(request), token));
    }

    @PostMapping("/insertAgreement")
    public Result insertAgreement(@RequestBody RCommRoleAgreement commRoleAgreement, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(rCommRoleAgreementService.save(commRoleAgreement, token));
    }

    @PutMapping("/updateAgreement")
    public Result updateAgreement(@RequestBody RCommRoleAgreement commRoleAgreement, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(rCommRoleAgreementService.saveOrUpdate(commRoleAgreement, token));
    }

    @DeleteMapping("/{id}")
    public Result deleteAgreement(@PathVariable("id") Long id, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(rCommRoleAgreementService.removeById(id, token));
    }
}

