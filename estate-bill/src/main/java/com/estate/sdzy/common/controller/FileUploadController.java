package com.estate.sdzy.common.controller;

import com.estate.common.controller.BaseController;
import com.estate.common.util.Result;
import com.estate.common.util.ResultUtil;
import com.estate.sdzy.common.service.ExcelService;
import com.estate.sdzy.common.excel.ExcelUtil;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sdzy/file/")
public class FileUploadController extends BaseController {

    @Autowired
    private ExcelService excelService;

    @PostMapping("fileUpload")
    public Result fileUpload(@RequestParam("file") MultipartFile file, String className) {
        List<Map<String, Object>> fileData = null;
        try {
//            fileData = ExcelUtil.getFileData(file,className);
            List<String> classFirld = ExcelUtil.getClassFirld(className,false);
            classFirld.forEach(re->{
                System.out.println(re);
            });
        } catch (Exception e) {

        }

        return ResultUtil.success();
    }

    @GetMapping("exportFile")
    public void exportFile(HttpServletResponse response,String className) throws IOException, WriterException {
        try {
            excelService.writeOut(response, null, className);
        }catch (Exception e){
            e.printStackTrace();
        }
//        byte[] bytes = QRCodeUtil.createQRCode("测试内容看行不行");
//        OutputStream os = new BufferedOutputStream(response.getOutputStream());
//        os.write(bytes);
//        os.flush();
//        os.close();
    }

}
