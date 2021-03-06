package com.estate.sdzy.asstes.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.estate.common.controller.BaseController;
import com.estate.common.exception.BillException;
import com.estate.common.util.BillExceptionEnum;
import com.estate.common.util.Result;
import com.estate.common.util.ResultUtil;
import com.estate.sdzy.asstes.entity.RCommunity;
import com.estate.sdzy.asstes.service.RCommunityService;
import com.estate.sdzy.system.service.SUserCommService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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
    public Result insertCommunity(@RequestBody RCommunity community, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(communityService.save(community, token));
    }

    @GetMapping("/listCommunity")
    public Result listCommunity(@RequestHeader("Authentication-Token") String token, HttpServletRequest request) {
        Page<RCommunity> rCommunityPage = communityService.listCommunity(token, super.getParameterMap(request));

        System.out.println(rCommunityPage.getRecords().size());
        return ResultUtil.success(rCommunityPage);
    }

    @PutMapping("/updateCommunity")
    public Result updateCommunity(@RequestBody RCommunity community, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(communityService.saveOrUpdate(community, token));
    }

    @DeleteMapping("/{id}")
    public Result deleteCommunity(@PathVariable("id") Long id, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(communityService.removeById(id, token));
    }

    @GetMapping("/{id}")
    public Result insertCommunity(@PathVariable("id") Long id, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(communityService.listComm(id, token));
    }

    @GetMapping("/userCommunity")
    public Result userCommunity(@RequestHeader("Authentication-Token") String token) {
        List<Long> commIdList = userCommService.getUserCommIdList(token);
        return ResultUtil.success(communityService.getUserCommunity(commIdList));
    }

    @GetMapping("/unitCommunity/{id}")
    public Result unitCommunity(@PathVariable("id") Long id) {
        if (StringUtils.isEmpty(id)) {
            throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
        }
        List<Long> list = new ArrayList<>();
        list.add(id);
        return ResultUtil.success(communityService.getUserCommunity(list));
    }

    @GetMapping("/getCommunityById")
    public Result getCommunityById(HttpServletRequest request) {
        Map<String, String> map = super.getParameterMap(request);
        return ResultUtil.success(communityService.getRoomByMap(map));
    }

    @GetMapping("/getUsersComm")
    public Result getUsersComm(@RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(communityService.getUsersComm(token));
    }

    @GetMapping("/listUser/{id}")
    public Result listUser(@PathVariable("id") Long id) {
        return ResultUtil.success(communityService.listUser(id));
    }

    @GetMapping("/listArea/{id}")
    public Result listArea(@PathVariable("id") Long id) {
        return ResultUtil.success(communityService.listArea(id));
    }

    @GetMapping("/listComm/{id}")
    public Result listComm(@PathVariable(value = "id", required = false) Long id, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(communityService.listComm(id, token));
    }

    @GetMapping("/getAllComm")
    public Result getAllComm(@RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(communityService.getAllComm(token));
    }

}

