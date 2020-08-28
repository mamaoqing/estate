package com.estate.sdzy.asstes.controller;


import com.estate.common.util.Result;
import com.estate.common.util.ResultUtil;
import com.estate.sdzy.asstes.entity.RCommArea;
import com.estate.sdzy.system.entity.SUser;
import com.estate.sdzy.asstes.service.RCommAreaService;
import com.estate.sdzy.system.service.SCompanyService;
import com.estate.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 社区分区表 前端控制器
 * </p>
 *
 * @author mq
 * @since 2020-07-28
 */
@RestController
@RequestMapping("/sdzy/rCommArea")
public class RCommAreaController {

    @Autowired
    private RCommAreaService commAreaService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SCompanyService companyService;

    @RequestMapping("/getAllAreaByUserId")
    public Result getAllArea(@RequestParam("commId") Long commId, @RequestParam("pageNo") Long pageNo, @RequestParam("size") Long size, @RequestHeader("Authentication-Token") String token) {
        SUser user = (SUser) redisUtil.get(token);
        Map<String,Object> map = new HashMap<>();
        Map<String,Object> dataMap = new HashMap<>();
        if(!StringUtils.isEmpty(commId)){
            map.put("commId",commId);
        }

        if(!StringUtils.isEmpty(pageNo)&&!StringUtils.isEmpty(size)){
            Long pageNum = (pageNo-1)*size;
            map.put("pageNo",pageNum);
            map.put("size",size);
        }
        if (!"超级管理员".equals(user.getType())) {
            // 添加只能查看存在权限
            if(!StringUtils.isEmpty(user)){
                map.put("userId",user.getId());
            }
        }
        dataMap.put("data",commAreaService.listAreaByUserId(map));
        dataMap.put("pageTotal",commAreaService.selectPageTotal(map));
        return ResultUtil.success(dataMap);
    }

    @RequestMapping("/insertArea")
    public Result insertArea(@RequestBody RCommArea commArea, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(commAreaService.insert(commArea, token));
    }

    @GetMapping("/getComp")
    public Result getComp(@RequestHeader("Authentication-Token") String token) {
        SUser user = (SUser) redisUtil.get(token);
        Long compId = 0l;
        if(user.getCompId()==0){
            compId = 1l;
        }else{
            compId = user.getCompId();
        }
        return ResultUtil.success(companyService.getById(compId));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id")Long id,@RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(commAreaService.delete(id, token));
    }

    @PostMapping("/update")
    public Result update(@RequestBody RCommArea commArea,@RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(commAreaService.update(commArea, token));
    }

    @GetMapping("/getArea/{commId}")
    public Result getAllArea(@PathVariable("commId") Long commId,@RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(commAreaService.getArea(commId,token));
    }

    @GetMapping("/listArea/{id}")
    public Result listArea(@PathVariable("id") Long id){

        return ResultUtil.success(commAreaService.listArea(id));
    }

}

