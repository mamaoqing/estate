package com.estate.sdzy.asstes.controller;


import com.estate.common.entity.SUser;
import com.estate.common.util.Result;
import com.estate.common.util.ResultUtil;
import com.estate.sdzy.asstes.entity.ROwner;
import com.estate.sdzy.asstes.entity.ROwnerProperty;
import com.estate.sdzy.asstes.service.ROwnerPropertyService;
import com.estate.sdzy.asstes.service.RRoomService;
import com.estate.sdzy.common.excel.ExportExcel;
import com.estate.sdzy.common.excel.ImportExcel;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 业主与物业对应关系 前端控制器
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
@RestController
@RequestMapping("/sdzy/rOwnerProperty")
public class ROwnerPropertyController {

    @Autowired
    private ROwnerPropertyService ownerPropertyService;
    @Autowired
    private RRoomService rRoomService;

    @GetMapping("/ownerPro/{id}")
    public Result ownerPro(@PathVariable("id") Long id) {
        return ResultUtil.success(ownerPropertyService.ownerProByParkId(id));
    }

    @GetMapping("/getOwnerProp/{ownerId}")
    public Result getOwnerProperty(@PathVariable("ownerId") Long ownerId) {
        return ResultUtil.success(ownerPropertyService.getOwnerProperty(ownerId));
    }

    @PostMapping("/getAllProp")
    public Result getAllProp(@RequestBody Map map, @RequestHeader("Authentication-Token") String token) {
        Map data = new HashMap();
        if (!StringUtils.isEmpty(map.get("pageNo")) && !StringUtils.isEmpty(map.get("size"))) {
            Long pageNum = (Long.parseLong(map.get("pageNo").toString()) - 1) * Long.parseLong(map.get("size").toString());
            map.put("pageNo", pageNum);
        }
        data.put("data", ownerPropertyService.getAllProperty(map, token));
        data.put("pageTotal", ownerPropertyService.getPageTotal(map, token));
        return ResultUtil.success(data);
    }

    @DeleteMapping("/deleteOwnerProp/{id}")
    public Result deleteOwnerProp(@PathVariable("id") Long id, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(ownerPropertyService.delete(id, token));
    }

    @PostMapping("/update")
    public Result update(@RequestBody ROwnerProperty ownerProperty, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(ownerPropertyService.update(ownerProperty, token));
    }

    @PostMapping("/export")
    public void exprotExcel(@RequestBody Map map,HttpServletResponse response, HttpServletRequest request, @RequestHeader("Authentication-Token") String token){
        SUser user = rRoomService.getUserByToken(token);
        try {
            ExportExcel.writeOut(response,"业主物业关系信息","com.estate.sdzy.asstes.entity.ROwnerProperty",
                    ownerPropertyService.getAllProperty(map,token),"导出人："+user.getUserName(),false);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @PostMapping("/insertRoomOwnerOrPark")
    public Result getOwnerProperty(@RequestBody Map map, @RequestHeader("Authentication-Token") String token) {
        boolean b = ownerPropertyService.insertRoomOwnerOrPark(map, token);
        if (b) {
            return ResultUtil.success();
        } else {
            return ResultUtil.error("业主重复", 1);
        }
    }

    @PostMapping("/exportTemplate")
    public void exportTemplate(HttpServletResponse response, HttpServletRequest request, @RequestHeader("Authentication-Token") String token){
        SUser user = rRoomService.getUserByToken(token);
        try {
            ExportExcel.writeOut(response,"业主物业关系信息列表导入模板","com.estate.sdzy.asstes.entity.ROwnerProperty",
                    null,"导出人："+user.getUserName(),true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @DeleteMapping("/deleteIds")
    public Result deleteIds(@Param("delIds") String delIds, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(ownerPropertyService.deleteIds(delIds, token));
    }

    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file, @RequestHeader("Authentication-Token") String token) throws IOException, ClassNotFoundException {
        List<Object> fileData = ImportExcel.getFileData(file, "com.estate.sdzy.asstes.entity.ROwnerProperty");
        fileData.forEach(x -> {
            ownerPropertyService.saveOrUpdateOwnerProp((ROwnerProperty) x, token);
        });
        return ResultUtil.success();
    }
}

