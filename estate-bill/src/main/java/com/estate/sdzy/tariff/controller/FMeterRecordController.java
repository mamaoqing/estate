package com.estate.sdzy.tariff.controller;


import com.estate.common.controller.BaseController;
import com.estate.common.entity.SUser;
import com.estate.common.util.Result;
import com.estate.common.util.ResultUtil;
import com.estate.sdzy.asstes.service.RRoomService;
import com.estate.sdzy.common.excel.ExportExcel;
import com.estate.sdzy.common.excel.ImportExcel;
import com.estate.sdzy.tariff.entity.FMeterRecord;
import com.estate.sdzy.tariff.service.FMeterRecordService;
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
 * 仪表流水表 前端控制器
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
@Slf4j
@RestController
@RequestMapping("/sdzy/fMeterRecord")
public class FMeterRecordController extends BaseController {

    @Autowired
    private FMeterRecordService fMeterRecordService;

    @Autowired
    private RRoomService rRoomService;

    @PostMapping("/insertMeterRecord")
    public Result insertMeterRecord(@RequestBody FMeterRecord fMeterRecord, @RequestHeader("Authentication-Token") String token) {
        boolean save = fMeterRecordService.save(fMeterRecord, token);
        if (save) {
            log.info("添加仪表成功，公司id={}", fMeterRecord.getId());
            return ResultUtil.success("添加仪表成功");
        }
        return ResultUtil.error("添加仪表失败！", 1);
    }

    @PutMapping("/updateMeterRecord")
    public Result updateMeterRecord(@RequestBody FMeterRecord fMeterRecord, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(fMeterRecordService.update(fMeterRecord,token));
    }

    @GetMapping("/listMeterRecord")
    public Result listMeterRecord(Integer pageNo, Integer size, HttpServletRequest request, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(fMeterRecordService.list(super.getParameterMap(request),pageNo,size,token));
    }

    @GetMapping("/listMeterRecordNum")
    public Result listMeterRecordNum(HttpServletRequest request, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(fMeterRecordService.listNum(super.getParameterMap(request),token));
    }

    @DeleteMapping("/{id}")
    public Result deleteMeterRecord(@PathVariable("id") String id, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(fMeterRecordService.delete(id, token));
    }

    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file, @RequestHeader("Authentication-Token") String token) throws IOException, ClassNotFoundException{
        List<Object> fileData = ImportExcel.getFileData(file, "com.estate.sdzy.tariff.entity.FMeterRecord");
        fileData.forEach(x->{
            fMeterRecordService.saveOrIgnoreMeter((FMeterRecord)x,token);
        });
        return ResultUtil.success();
    }

    @PostMapping("/export")
    public void exprotExcel(HttpServletResponse response, HttpServletRequest request, @RequestHeader("Authentication-Token") String token){
        SUser user = rRoomService.getUserByToken(token);
        try {
            ExportExcel.writeOut(response,"仪表抄表信息列表","com.estate.sdzy.tariff.entity.FMeterRecord",
                    fMeterRecordService.listAll(super.getParameterMap(request),token),"导出人："+user.getUserName());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

