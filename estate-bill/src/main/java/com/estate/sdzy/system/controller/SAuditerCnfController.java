package com.estate.sdzy.system.controller;

import com.estate.common.controller.BaseController;
import com.estate.common.util.Result;
import com.estate.common.util.ResultUtil;
import com.estate.sdzy.system.entity.SAuditerCnf;
import com.estate.sdzy.system.service.SAuditerCnfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@RequestMapping("/sdzy/sAuditerCnf")
public class SAuditerCnfController extends BaseController {

    @Autowired
    private SAuditerCnfService sAuditerCnfService;

    @PostMapping("/insertAuditerCnf")
    public Result insertAuditerCnf(@RequestBody SAuditerCnf sAuditerCnf, @RequestHeader("Authentication-Token") String token) {
        boolean save = sAuditerCnfService.save(sAuditerCnf, token);
        if (save) {
            log.info("添加审核人成功，公司id={}", sAuditerCnf.getId());
            return ResultUtil.success("添加审核人成功");
        }
        return ResultUtil.error("添加审核人失败！", 1);
    }

    @PutMapping("/updateAuditerCnf")
    public Result updateAuditerCnf(@RequestBody SAuditerCnf sAuditerCnf, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(sAuditerCnfService.update(sAuditerCnf,token));
    }

    @PutMapping("/checkAuditerCnf")
    public Result checkAuditerCnf(@RequestBody SAuditerCnf sAuditerCnf) {
        return ResultUtil.success(sAuditerCnfService.checkSAuditerCnf(sAuditerCnf));
    }

    @GetMapping("/listAuditerCnf")
    public Result listAuditerCnf(Integer pageNo, Integer size, HttpServletRequest request, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(sAuditerCnfService.list(super.getParameterMap(request),pageNo,size,token));
    }

    @GetMapping("/listAllAuditerCnf")
    public Result listAllAuditerCnf(HttpServletRequest request, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(sAuditerCnfService.listAll(super.getParameterMap(request),token));
    }

    @GetMapping("/listAuditerCnfNum")
    public Result listAuditerCnfNum(HttpServletRequest request, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(sAuditerCnfService.listNum(super.getParameterMap(request),token));
    }

    @DeleteMapping("/{id}")
    public Result deleteAuditerCnf(@PathVariable("id") String id, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(sAuditerCnfService.delete(id, token));
    }
}
