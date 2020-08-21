package com.estate.sdzy.asstes.controller;


import com.estate.sdzy.asstes.entity.RBuilding;
import com.estate.sdzy.asstes.service.RBuildingService;
import com.estate.sdzy.asstes.service.RCommAreaService;
import com.estate.sdzy.common.controller.BaseController;
import com.estate.util.Result;
import com.estate.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 楼房 前端控制器
 * </p>
 *
 * @author mq
 * @since 2020-07-28
 */
@RestController
@RequestMapping("/sdzy/rBuilding")
@Slf4j
public class RBuildingController extends BaseController {

    @Autowired
    private RBuildingService rBuildingService;

    @Autowired
    private RCommAreaService rCommAreaService;

    @PostMapping("/insertBuilding")
    public Result insertBuilding(@RequestBody RBuilding rBuilding, @RequestHeader("Authentication-Token") String token) {
        boolean save = rBuildingService.save(rBuilding, token);
        if (save) {
            log.info("添加字典成功，公司id={}", rBuilding.getId());
            ResultUtil.success("添加字典成功");
        }
        return ResultUtil.error("添加字典失败！", 1);
    }

    @PostMapping("/checkBulidingNameNo")
    public Result checkBulidingNameNo(@RequestBody RBuilding rBuilding, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(rBuildingService.checkBulidingNameNo(rBuilding));
    }

    @PostMapping("/copyBuilding")
    public Result copyBuilding(@RequestBody RBuilding rBuilding, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(rBuildingService.copyBuildings(rBuilding,token));
    }

    @PutMapping("/updateBuilding")
    public Result updateBuilding(@RequestBody RBuilding rBuilding, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(rBuildingService.update(rBuilding,token));
    }

    @GetMapping("/listBuilding")
    public Result listBuilding(Integer pageNo, Integer size, HttpServletRequest request, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(rBuildingService.list(super.getParameterMap(request),pageNo,size,token));
    }

    @GetMapping("/listBuildingNum")
    public Result listBuildingNum(HttpServletRequest request, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(rBuildingService.listNum(super.getParameterMap(request),token));
    }

    @DeleteMapping("/{id}")
    public Result deleteBuilding(@PathVariable("id") Long id, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(rBuildingService.delete(id, token));
    }

    @GetMapping("/getUserComm/{compId}")
    public Result getUserComm(@PathVariable("compId") Long compId,@RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(rBuildingService.getUserComm(token,compId));//返回为true则表示重复
    }

    @GetMapping("/getCommArea/{commId}")
    public Result getCommArea(@PathVariable("commId") Long commId) {
        return ResultUtil.success(rCommAreaService.getCommArea(commId));//返回为true则表示重复
    }

    @GetMapping("/getBuildings/{commAreaId}")
    public Result getBuildings(@PathVariable("commAreaId") Long commAreaId) {
        return ResultUtil.success(rBuildingService.getList(commAreaId));//返回为true则表示重复
    }

    @GetMapping("/getUnits/{buildingId}")
    public Result getUnits(@PathVariable("buildingId") Long buildingId) {
        return ResultUtil.success(rBuildingService.getUnitList(buildingId));//返回为true则表示重复
    }

    @GetMapping("/getCommAreaContent/{commAreaId}")
    public Result getCommAreaContent(@PathVariable("commAreaId") Long commAreaId) {
        return ResultUtil.success(rCommAreaService.getCommAreaContent(commAreaId));//返回为true则表示重复
    }

    @GetMapping("/checkBuildingRoomUnit/{buildingId}")
    public Result checkBuildingRoomUnit(@PathVariable("buildingId") Long buildingId) {
        return ResultUtil.success(rBuildingService.checkBuildingRoomUnit(buildingId));//返回为true则表示重复
    }

}

