package com.estate.sdzy.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.estate.exception.BillException;
import com.estate.sdzy.entity.RCommunity;
import com.estate.sdzy.entity.RRoom;
import com.estate.sdzy.service.RCommunityService;
import com.estate.sdzy.service.SUserCommService;
import com.estate.util.BillExceptionEnum;
import com.estate.util.Result;
import com.estate.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 社区表 前端控制器
 * </p>
 *
 * @author mq
 * @since 2020-07-28
 */
@RestController
@RequestMapping("/sdzy/rCommunity")
public class RCommunityController extends BaseController {

    @Autowired
    private RCommunityService communityService;

    @Autowired
    private SUserCommService userCommService;

    @PostMapping("/insertCommunity")
    public Result insertCommunity(RCommunity community, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(communityService.save(community, token));
    }

    @GetMapping("/listCommunity")
    public Result listCommunity(@RequestHeader("Authentication-Token") String token,HttpServletRequest request) {
        Page<RCommunity> rCommunityPage = communityService.listCommunity(token, super.getParameterMap(request));

        System.out.println(rCommunityPage.getRecords().size());
        return ResultUtil.success(rCommunityPage);
    }

    @PutMapping("/updateCommunity")
    public Result updateCommunity(RCommunity community, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(communityService.saveOrUpdate(community, token));
    }

    @DeleteMapping("/{id}")
    public Result deleteCommunity(@PathVariable("id") Long id, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(communityService.removeById(id, token));
    }

    @GetMapping("/{id}")
    public Result insertCommunity(@PathVariable("id") Long id, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(communityService.getById(id));
    }

    @GetMapping("/userCommunity")
    public Result userCommunity(@RequestHeader("Authentication-Token") String token) {
        List<Long> commIdList = userCommService.getUserCommIdList(token);
        return ResultUtil.success(communityService.getUserCommunity(commIdList));
    }

    @GetMapping("/getCommunityById")
    public Result getCommunityById(HttpServletRequest request) {
        Map<String, String> map = super.getParameterMap(request);
        return ResultUtil.success(communityService.getRoomByMap(map));
    }

}

