package com.estate.sdzy.tariff.controller;


import com.estate.sdzy.common.controller.BaseController;
import com.estate.sdzy.common.excel.ExportExcel;
import com.estate.sdzy.common.excel.ImportExcel;
import com.estate.sdzy.tariff.entity.FMeter;
import com.estate.sdzy.tariff.service.FMeterService;
import com.estate.util.Result;
import com.estate.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 仪表表 前端控制器
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
@RestController
@RequestMapping("/sdzy/fMeter")
@Slf4j
public class FMeterController extends BaseController {

    @Autowired
    private FMeterService fMeterService;

    @PostMapping("/insertMeter")
    public Result insertMeter(@RequestBody FMeter fMeter, @RequestHeader("Authentication-Token") String token) {
        boolean save = fMeterService.save(fMeter, token);
        if (save) {
            log.info("添加仪表成功，公司id={}", fMeter.getId());
            return ResultUtil.success("添加仪表成功");
        }
        return ResultUtil.error("添加仪表失败！", 1);
    }

    @PutMapping("/updateMeter")
    public Result updateMeter(@RequestBody FMeter fMeter, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(fMeterService.update(fMeter,token));
    }

    @GetMapping("/listMeter")
    public Result listMeter(Integer pageNo, Integer size, HttpServletRequest request, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(fMeterService.list(super.getParameterMap(request),pageNo,size,token));
    }

    @GetMapping("/listMeterNum")
    public Result listMeterNum(HttpServletRequest request, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(fMeterService.listNum(super.getParameterMap(request),token));
    }

    @DeleteMapping("/{id}")
    public Result deleteMeter(@PathVariable("id") String id, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(fMeterService.delete(id, token));
    }

    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file, @RequestHeader("Authentication-Token") String token) throws IOException, ClassNotFoundException{
        List<Object> fileData = ImportExcel.getFileData(file, "com.estate.sdzy.tariff.entity.FMeter");
        fileData.forEach(x->{
            fMeterService.saveOrUpdateMeter((FMeter)x,token);
        });
        return ResultUtil.success();
    }

    @PostMapping("/export")
    public void testExprotExcel(HttpServletResponse response, HttpServletRequest request, @RequestHeader("Authentication-Token") String token){
        try {
            ExportExcel.writeOut(response,"仪表信息列表","com.estate.sdzy.tariff.entity.FMeter",
                    fMeterService.listAll(super.getParameterMap(request),token),"导出人：mmq");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
