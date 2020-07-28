package com.estate.sdzy.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.estate.sdzy.entity.RCommunity;
import com.estate.sdzy.service.RCommunityService;
import com.estate.sdzy.service.SUserCommService;
import com.estate.util.Result;
import com.estate.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;

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
public class RCommunityController {

    @Autowired
    private RCommunityService communityService;

    @Autowired
    private SUserCommService userCommService;

    @PostMapping("/insertCommunity")
    public Result insertCommunity(RCommunity community, String token) {
        try {
            boolean save = communityService.save(community, token);
            return ResultUtil.success(save);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtil.error("社区信息添加失败。", 1);
    }

    @GetMapping("/listCommunity")
    public Result listCommunity(Integer pageNo, Integer size) {
        try {
            if (StringUtils.isEmpty(pageNo)) {
                return ResultUtil.error("参数错误，请输入页码", 1);
            }
            if (StringUtils.isEmpty(size)) {
                size = 10;
            }
            Page<RCommunity> page = new Page<>(pageNo, size);
            Page<RCommunity> page1 = communityService.page(page);
            return ResultUtil.success(page1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtil.error("查询错误！", 1);
    }

    @PutMapping("/updateCommunity")
    public Result updateCommunity(RCommunity community, String token) {
        try {
            boolean update = communityService.saveOrUpdate(community, token);
            return ResultUtil.success(update);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtil.error("社区信息更新失败", 1);
    }

    @DeleteMapping("/{id}")
    public Result deleteCommunity(@PathVariable("id") Long id, String token) {
        try {
            boolean remove = communityService.removeById(id, token);
            return ResultUtil.success(remove);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtil.error("社区信息删除失败", 1);
    }

    @GetMapping("/{id}")
    public Result insertCommunity(@PathVariable("id") Long id, String token) {
        try {
            RCommunity community = communityService.getById(id);
            return ResultUtil.success(community);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtil.error("查询失败", 1);
    }

    @GetMapping("/userCommunity")
    public Result userCommunity(String token){
        try {
            List<Long> commIdList = userCommService.getUserCommIdList(token);
            List<RCommunity> userCommunity = communityService.getUserCommunity(commIdList);
            return ResultUtil.success(userCommunity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtil.error("查询失败", 1);
    }

}

