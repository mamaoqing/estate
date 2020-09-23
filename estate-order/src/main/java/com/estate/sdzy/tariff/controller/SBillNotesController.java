package com.estate.sdzy.tariff.controller;


import com.estate.common.util.Result;
import com.estate.common.util.ResultUtil;
import com.estate.sdzy.tariff.entity.SBillNotes;
import com.estate.sdzy.tariff.service.SBillNotesService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author mq
 * @since 2020-09-21
 */
@RestController
@RequestMapping("/order/sBillNotes")
public class SBillNotesController {
    @Autowired
    public SBillNotesService notesService;

    @GetMapping("getAll")
    public Result getAll(@RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(notesService.getAll(token));
    }

    @GetMapping("getByCommId")
    public Result getByCommId(@Param("commId") Long commId, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(notesService.getByCommId(commId,token));
    }

    @PostMapping("insert")
    public Result insert(@RequestBody SBillNotes notes,@RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(notesService.insert(notes,token));
    }

    @PostMapping("update")
    public Result update(@RequestBody SBillNotes notes,@RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(notesService.update(notes,token));
    }
    @DeleteMapping("delete/{id}")
    public Result delete(@PathVariable("id") Long id,@RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(notesService.delete(id,token));
    }
}

