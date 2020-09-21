package com.estate.sdzy.asstes.controller;


import com.estate.common.entity.SUser;
import com.estate.common.util.Result;
import com.estate.common.util.ResultUtil;
import com.estate.sdzy.asstes.entity.ROwner;
import com.estate.sdzy.asstes.service.ROwnerService;
import com.estate.sdzy.common.excel.ExportExcel;
import com.estate.sdzy.common.excel.ImportExcel;
import com.estate.util.RedisUtil;
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
 * 业主表 前端控制器
 * </p>
 *
 * @author mq
 * @since 2020-08-04
 */
@RestController
@RequestMapping("/sdzy/rOwner")
public class ROwnerController {

    @Autowired
    private ROwnerService ownerService;
    @Autowired
    private RedisUtil redisUtil;

    @PostMapping("/getOwenerList")
    public Result getOwenerList(@RequestBody Map<String, String> map, @RequestHeader("Authentication-Token") String token) {
        Map data = new HashMap();
        if (!StringUtils.isEmpty(map.get("pageNo")) && !StringUtils.isEmpty(map.get("size"))) {
            Long pageNum = (Long.valueOf(map.get("pageNo")) - 1) * Long.valueOf(map.get("size"));
            map.put("pageNo", String.valueOf(pageNum));
        }
        data.put("data", ownerService.getOwenerList(map, token));
        data.put("pageTotal", ownerService.selectPageTotal(map, token));
        return ResultUtil.success(data);
    }

    @PostMapping("/getPageOwenerList")
    public Result getPageOwenerList(@RequestBody Map<String, String> map, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(ownerService.getOwenerPageList(map, token));
    }

    @PostMapping("/getOwners")
    public Result getOwners(@RequestBody Map<String, String> map, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(ownerService.getOwners(map, token));
    }

    @PostMapping("/getOwenerByRoom")
    public List<ROwner> getOwenerByRoom(@RequestBody Map<String, String> map, @RequestHeader("Authentication-Token") String token) {
        return ownerService.getOwenerByRoom(map, token);
    }

    @PostMapping("/insert")
    public Result insert(@RequestBody ROwner owner, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(ownerService.insert(owner, token));
    }

    @PostMapping("/update")
    public Result update(@RequestBody ROwner owner, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(ownerService.update(owner, token));
    }

    @PostMapping("/getExcel")
    public void getExcel(@RequestBody Map<String, String> map, HttpServletResponse response, @RequestHeader("Authentication-Token") String token) {
        SUser user = (SUser) redisUtil.get(token);
        try {
            map.remove("pageNo");
            map.remove("size");
            ExportExcel.writeOut(response, "业主信息列表", "com.estate.sdzy.asstes.entity.ROwner",
                    ownerService.getExcel(map, token), "导出人：" + user.getUserName(), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/getCount")
    public Result getCount(@RequestBody ROwner owner, @RequestHeader("Authentication-Token") String token) {
        ROwner count = ownerService.getCount(owner, token);
        if (count != null) {
            return ResultUtil.success(count);
        } else {
            return ResultUtil.error("未找到相关信息", 1);
        }
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(ownerService.delete(id, token));
    }

    @DeleteMapping("/deleteIds")
    public Result deleteIds(@Param("delIds") String delIds, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(ownerService.deleteIds(delIds, token));
    }

    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file, @RequestHeader("Authentication-Token") String token) throws IOException, ClassNotFoundException {
        List<Object> fileData = ImportExcel.getFileData(file, "com.estate.sdzy.asstes.entity.ROwner");
        fileData.forEach(x -> {
            ownerService.saveOrUpdateOwner((ROwner) x, token);
        });
        return ResultUtil.success();
    }

    @PostMapping("/exportTemplate")
    public void exportTemplate(HttpServletResponse response, HttpServletRequest request, @RequestHeader("Authentication-Token") String token) {
        SUser user = (SUser) redisUtil.get(token);
        try {
            ExportExcel.writeOut(response, "业主信息列表导入模板", "com.estate.sdzy.asstes.entity.ROwner",
                    null, "导出人：" + user.getUserName(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("getRoomByOwnerId/{ownerId}")
    public Result getRoomByOwnerId(@PathVariable Integer ownerId, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(ownerService.selectRoomByOwnerId(ownerId, token));
    }

    @PostMapping("/listOwnerByCommId")
    public Result listOwnerByCommId(@RequestBody Map<String,String> map,@RequestHeader("Authentication-Token") String token){
        return ResultUtil.success(ownerService.listOwnerByCommId(map,token));
    }

    @GetMapping("getParkByOwnerId/{ownerId}")
    public Result getParkByOwnerId(@PathVariable Integer ownerId, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(ownerService.selectParkByOwnerId(ownerId, token));
    }
}

