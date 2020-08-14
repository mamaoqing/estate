package com.estate.sdzy.controller;


import com.estate.sdzy.entity.RCommArea;
import com.estate.sdzy.entity.SUser;
import com.estate.sdzy.service.RCommAreaService;
import com.estate.sdzy.service.SCompanyService;
import com.estate.util.RedisUtil;
import com.estate.util.Result;
import com.estate.util.ResultUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
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
    public Result getAllArea(@RequestBody RCommArea commArea,@Param("pageNo") Long pageNo,@Param("size") Long size, @RequestHeader("Authentication-Token") String token) {
        SUser user = (SUser) redisUtil.get(token);
        Map<String,Object> map = new HashMap<>();
        if(!StringUtils.isEmpty(commArea.getCommId())){
            map.put("commId",commArea.getCommId());
        }

        if(!StringUtils.isEmpty(pageNo)&&!StringUtils.isEmpty(size)){
            Long pageNum = (pageNo-1)*size;
            map.put("pageNo",pageNum);
            map.put("size",size);
        }
        if (!"超级管理员".equals(user.getType())) {
            // 添加只能查看存在权限的社区条件
            if(!StringUtils.isEmpty(user)){
                map.put("userId",user.getId());
            }
        }

        System.out.println(map);
        return ResultUtil.success(commAreaService.listAreaMapByUserId(map));
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
}

