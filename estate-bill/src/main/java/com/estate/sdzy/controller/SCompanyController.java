package com.estate.sdzy.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.estate.sdzy.entity.SCompany;
import com.estate.sdzy.service.SCompanyService;
import com.estate.sdzy.service.SUserService;
import com.estate.util.Pinyin;
import com.estate.util.Result;
import com.estate.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

/**
 * <p>
 * 物业公司表 前端控制器
 * </p>
 *
 * @author mq
 * @since 2020-07-23
 */
@RestController
@RequestMapping("/sdzy/sCompany")
@Slf4j
public class SCompanyController {

    @Autowired
    private SCompanyService companyService;

    @Autowired
    private SUserService userService;

    @PostMapping("/insertCompany")
    public Result insertCompany(SCompany sCompany){
        boolean save = companyService.save(sCompany);
        if(save){
            log.info("添加公司信息成功，公司id={}",sCompany.getId());
            Boolean aBoolean = userService.autoSave(sCompany);
            if(aBoolean){
                log.info("自动添加公司管理员角色，登录用户名={},密码=123456", Pinyin.getPinYinHeadChar(sCompany.getAbbreviation()));
            }else{
                log.error("自动添加管理员角色失败");
            }
            return ResultUtil.success(sCompany);
        }

        return ResultUtil.error("保存公司信息失败！",1);
    }

    @PutMapping("/updateCompany")
    public Result updateCompany(SCompany sCompany){
        boolean b = companyService.saveOrUpdate(sCompany);
        if(b){
            log.info("更新公司信息成功，公司id={}",sCompany.getId());
            return ResultUtil.success();
        }
        log.info("更新公司信息失败，公司id={}",sCompany.getId());
        return ResultUtil.error("更新公司信息出错",1);
    }

    @GetMapping("/listCompany")
    public Result listCompany(Integer pageNo,Integer size){
        if(StringUtils.isEmpty(pageNo)){
            return ResultUtil.error("参数错误，请输入页码",1);
        }
        if(StringUtils.isEmpty(size)){
            size = 10;
        }
        Page<SCompany> p = new Page<>(pageNo,size);
        Page<SCompany> page = companyService.page(p);
        return ResultUtil.success(page);
//        return ResultUtil.error("",1);

    }

    @GetMapping("/{id}")
    public Result getCompany(@PathVariable("id") Long id){
        SCompany sCompany = companyService.getById(id);
        return ResultUtil.success(sCompany);
    }

    @DeleteMapping("/{id}")
    public Result deleteCompany(@PathVariable("id") Long id){
        boolean b = companyService.removeById(id);
        if (b) {
            log.info("删除公司信息成功。公司id={}",id);
            return  ResultUtil.success();
        }
        return ResultUtil.error("删除公司信息错误",1);
    }


}

