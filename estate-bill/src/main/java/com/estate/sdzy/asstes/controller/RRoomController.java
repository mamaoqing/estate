package com.estate.sdzy.asstes.controller;


import com.estate.sdzy.asstes.entity.RRoom;
import com.estate.sdzy.asstes.service.RRoomService;
import com.estate.sdzy.common.controller.BaseController;
import com.estate.util.Result;
import com.estate.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    public Result upload(HttpServletRequest request, @RequestHeader("Authentication-Token") String token){
        return rRoomService.importExcel(request,token);
    }

    @RequestMapping("/export")
    public void testExprotExcel(HttpServletResponse response,HttpServletRequest request, @RequestHeader("Authentication-Token") String token){

        //创建一个数组用于设置表头
        /*List<String> str = new ArrayList<>();
        try{
            Class<?> aClass = Class.forName("com.estate.sdzy.asstes.entity.RRoom");
            Excel[] annotation = aClass.getAnnotationsByType(Excel.class);
            for(int i=0;i<annotation.length;i++){
                str.add(annotation[i].name());
            }
        }catch (Exception e){
        }
        String[] arr=str.toArray(new String[str.size()]);

        //调用Excel导出工具类
        ExcelExport.export(response,rRoomService.list(super.getParameterMap(request),token),arr);*/

    }

}

