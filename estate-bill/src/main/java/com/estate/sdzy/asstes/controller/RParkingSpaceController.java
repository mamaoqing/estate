package com.estate.sdzy.asstes.controller;


import com.estate.sdzy.asstes.entity.RParkingSpace;
import com.estate.sdzy.asstes.service.RParkingSpaceService;
import com.estate.sdzy.common.controller.BaseController;
import com.estate.sdzy.common.excel.ImportExcel;
import com.estate.util.Result;
import com.estate.util.ResultUtil;
import com.estate.util.qrcode.QRCodeUtil;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 停车位 前端控制器
 * </p>
 *
 * @author mq
 * @since 2020-08-17
 */
@RestController
@RequestMapping("/sdzy/rParkingSpace")
public class RParkingSpaceController extends BaseController {

    @Autowired
    private RParkingSpaceService parkingSpaceService;

    @GetMapping("/listPark")
    public Result listPark(@RequestHeader("Authentication-Token") String token, HttpServletRequest request){
        return ResultUtil.success(parkingSpaceService.listPark(super.getParameterMap(request),token));
    }
    @PostMapping("/insertPark")
    public Result insertPark(@RequestHeader("Authentication-Token") String token, @RequestBody RParkingSpace parkingSpace){
        return ResultUtil.success(parkingSpaceService.save(token,parkingSpace));
    }
    @PutMapping("/updatePark")
    public Result updatePark(@RequestHeader("Authentication-Token") String token, @RequestBody RParkingSpace parkingSpace){
        return ResultUtil.success(parkingSpaceService.saveOrUpdate(token,parkingSpace));
    }
    @DeleteMapping("/{id}")
    public Result deletePark(@PathVariable("id") Long id ,@RequestHeader("Authentication-Token") String token){
        return ResultUtil.success(parkingSpaceService.removeById(id,token));
    }

    @DeleteMapping("/deleteAll/{ids}")
    public Result deleteAll(@PathVariable("ids") String ids ,@RequestHeader("Authentication-Token") String token){
        return ResultUtil.success(parkingSpaceService.removeByIds(ids,token));
    }

    @GetMapping("/qrcode")
    public void qrcode(HttpServletRequest request , HttpServletResponse response) throws IOException, WriterException {

        byte[] bytes = QRCodeUtil.createQRCode("测试内容看行不行");
        OutputStream os = new BufferedOutputStream(response.getOutputStream());
        os.write(bytes);
        os.flush();
        os.close();
    }


    @GetMapping("/exportFile")
    public void exportFile(HttpServletResponse response,String className) {
        try {
            parkingSpaceService.writeOut(response, null, className);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @PostMapping("/fileUpload")
    public Result uploadFile(@RequestParam("file") MultipartFile file, String className) throws IOException, ClassNotFoundException {
        List<Object> fileData = ImportExcel.getFileData(file, className);
        fileData.forEach(x->{
//            RParkingSpace s = (RParkingSpace)x;
//            parkingSpaceService.save(s);
            System.out.println((RParkingSpace)x);

        });
        return ResultUtil.success();
    }

}

