package com.estate.sdzy.tariff.controller;


import com.estate.common.controller.BaseController;
import com.estate.common.entity.SUser;
import com.estate.common.util.Result;
import com.estate.common.util.ResultUtil;
import com.estate.sdzy.asstes.service.RRoomService;
import com.estate.sdzy.common.excel.ExportExcel;
import com.estate.sdzy.tariff.entity.FBillAlter;
import com.estate.sdzy.tariff.service.FBillAlterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mq
 * @since 2020-09-02
 */
@RestController
@RequestMapping("/order/fBillAlter")
@Slf4j
public class FBillAlterController extends BaseController {

    @Autowired
    private FBillAlterService fBillAlterService;

    @Autowired
    private RRoomService rRoomService;

    @PostMapping("/insertBillAlter")
    public Result insertBillAlter(@RequestBody FBillAlter fBillAlter, @RequestHeader("Authentication-Token") String token) {
        boolean save = fBillAlterService.save(fBillAlter, token);
        return ResultUtil.success(save);
    }

    @PutMapping("/updateBillAlter")
    public Result updateBillAlter(@RequestBody FBillAlter fBillAlter, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(fBillAlterService.update(fBillAlter,token));
    }

    @GetMapping("/listBillAlter")
    public Result listBillAlter(Integer pageNo, Integer size, HttpServletRequest request, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(fBillAlterService.list(super.getParameterMap(request),pageNo,size,token));
    }

    @GetMapping("/listBillAlterByBillId/{billId}")
    public Result listBillAlterByBillId(@PathVariable("billId") Long billId, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(fBillAlterService.getBillAlertByBillId(billId,token));
    }

    @DeleteMapping("/{id}")
    public Result deleteAuditerCnf(@PathVariable("id") String id, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(fBillAlterService.delete(id, token));
    }

    @PostMapping("/export")
    public void testExprotExcel(HttpServletResponse response, HttpServletRequest request, @RequestHeader("Authentication-Token") String token){
        SUser user = rRoomService.getUserByToken(token);
        try {
            ExportExcel.writeOut(response,"费用调整列表","com.estate.sdzy.tariff.entity.FBillAlter",
                    fBillAlterService.listAll(super.getParameterMap(request),token),"导出人："+user.getUserName(),false);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

