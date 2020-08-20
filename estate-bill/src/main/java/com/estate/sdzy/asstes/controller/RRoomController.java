package com.estate.sdzy.asstes.controller;


import com.estate.sdzy.asstes.entity.RParkingSpace;
import com.estate.sdzy.asstes.entity.RRoom;
import com.estate.sdzy.asstes.service.RRoomService;
import com.estate.sdzy.common.controller.BaseController;
import com.estate.sdzy.common.excel.ExportExcel;
import com.estate.sdzy.common.excel.ImportExcel;
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
 * 房间 前端控制器
 * </p>
 *
 * @author mq
 * @since 2020-07-28
 */
@RestController
@RequestMapping("/sdzy/rRoom")
@Slf4j
public class RRoomController extends BaseController {

    @Autowired
    private RRoomService rRoomService;

    @PostMapping("/insertRoom")
    public Result insertRoom(@RequestBody RRoom rRoom, @RequestHeader("Authentication-Token") String token) {
        boolean save = rRoomService.save(rRoom, token);
        if (save) {
            log.info("添加字典成功，公司id={}", rRoom.getId());
            ResultUtil.success("添加字典成功");
        }
        return ResultUtil.error("添加字典失败！", 1);
    }

    @PutMapping("/updateRoom")
    public Result updateRoom(@RequestBody RRoom rRoom, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(rRoomService.update(rRoom,token));
    }

    @GetMapping("/listRoom")
    public Result listRoom(Integer pageNo, Integer size, HttpServletRequest request, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(rRoomService.list(super.getParameterMap(request),pageNo,size,token));
    }

    @GetMapping("/listRoomNum")
    public Result listRoomNum(HttpServletRequest request, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(rRoomService.listNum(super.getParameterMap(request),token));
    }

    @GetMapping("/checkRoomOwer/{roomId}")
    public Result checkRoomOwer(@PathVariable("roomId") String roomId) {
        return ResultUtil.success(rRoomService.checkRoomOwer(roomId));
    }

    @DeleteMapping("/{id}")
    public Result deleteRoom(@PathVariable("id") String id, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(rRoomService.delete(id, token));
    }

    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file, @RequestHeader("Authentication-Token") String token) throws IOException, ClassNotFoundException{
        List<Object> fileData = ImportExcel.getFileData(file, "com.estate.sdzy.entity.RRoom");
        fileData.forEach(x->{
//            RParkingSpace s = (RParkingSpace)x;
//            parkingSpaceService.save(s);
            System.out.println((RParkingSpace)x);

        });
        return ResultUtil.success();
    }

    @RequestMapping("/export")
    public void testExprotExcel(HttpServletResponse response,HttpServletRequest request, @RequestHeader("Authentication-Token") String token){
        //rRoomService.list(super.getParameterMap(request),token);
        try {
            ExportExcel.writeOut(response,"停车位信息列表","com.estate.sdzy.entity.RRoom",rRoomService.list(super.getParameterMap(request),token),"导出人：mmq");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

