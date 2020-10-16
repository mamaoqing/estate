package com.estate.sdzy.system.controller;


import com.estate.common.controller.BaseController;
import com.estate.common.util.Result;
import com.estate.common.util.ResultUtil;
import com.estate.sdzy.system.service.SMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

/**
 * <p>
 * 角色表  前端控制器
 * </p>
 *
 * @author mq
 * @since 2020-07-23
 */
@RestController
@RequestMapping("/sdzy/sMessage")
@Slf4j
public class SMessageController extends BaseController {

    @Autowired
    private SMessageService messageService;

    @GetMapping("listMessageComm")
    public Result listMessageComm(Long id, Long compId,@RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(messageService.listMessageComm(compId, id));
    }

    @PostMapping("/setMessageComm")
    public Result setMessageComm(Long id, String commIds, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(messageService.setMessageComm(id, commIds, token));
    }

    @GetMapping("/listMessage")
    public Result listMessage(Integer pageNo, Integer size, HttpServletRequest request, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(messageService.listMessage(super.getParameterMap(request),pageNo,size,token));
    }

    @PutMapping("/updateMessage")
    public Result updateMessage(HttpServletRequest request,@RequestParam MultipartFile[] files, @RequestHeader("Authentication-Token") String token) {

        return ResultUtil.success(messageService.saveOrUpdate(request, token,files));
    }

    @PostMapping("/insertMessage")
    public Result insertMessage(HttpServletRequest request,@RequestParam MultipartFile[] files, @RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(messageService.save(request,token,files));
    }

    @DeleteMapping("/{id}")
    public Result deleteMessage(@PathVariable("id") Long id,@RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(messageService.removeById(id,token));
    }

    @GetMapping("/delFile")
    public Result deleteFile(@RequestParam("path") String path,@RequestHeader("Authentication-Token") String token) {
        return ResultUtil.success(messageService.delPic(path,token));
    }



    @PostMapping("/upload")
    public Result upload(@RequestParam("image") MultipartFile file) throws IOException, ClassNotFoundException{
        String fileName = file.getOriginalFilename();// 文件原名称
        String path = "D:\\topPic\\"+fileName;
        System.out.println("存放图片文件的路径:" + path);
        File file1 = new File(path);
        // 转存文件到指定的路径
        try {
            file.transferTo(file1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultUtil.success(path);
    }
}

