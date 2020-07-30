package com.estate.sdzy.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
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
public class RCommunityController extends BaseController{

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
        throw new BillException(BillExceptionEnum.SYSTEM_INSERT_ERROR);
    }

    @GetMapping("/listCommunity")
    public Result listCommunity(Integer pageNo, Integer size) {
        try {
            if (StringUtils.isEmpty(pageNo)) {
                throw new BillException(BillExceptionEnum.PARAMS_MISS_ERROR);
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
        throw new BillException(BillExceptionEnum.SYSTEM_SELECT_ERROR);
    }

    @PutMapping("/updateCommunity")
    public Result updateCommunity(RCommunity community, String token) {
        try {
            boolean update = communityService.saveOrUpdate(community, token);
            return ResultUtil.success(update);
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new BillException(BillExceptionEnum.SYSTEM_UPDATE_ERROR);
    }

    @DeleteMapping("/{id}")
    public Result deleteCommunity(@PathVariable("id") Long id, String token) {
        try {
            boolean remove = communityService.removeById(id, token);
            return ResultUtil.success(remove);
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new BillException(BillExceptionEnum.SYSTEM_DELETE_ERROR);
    }

    @GetMapping("/{id}")
    public Result insertCommunity(@PathVariable("id") Long id, String token) {
        try {
            RCommunity community = communityService.getById(id);
            return ResultUtil.success(community);
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new BillException(BillExceptionEnum.SYSTEM_SELECT_ERROR);
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
        throw new BillException(BillExceptionEnum.SYSTEM_SELECT_ERROR);
    }

    @GetMapping("/getCommunityById")
    public Result getCommunityById(HttpServletRequest request){
        try {
            Map<String, String> map = super.getParameterMap(request);
            Page<RRoom> roomPage = communityService.getRoomByMap(map);
            return ResultUtil.success(roomPage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new BillException(BillExceptionEnum.SYSTEM_SELECT_ERROR);
    }

}

