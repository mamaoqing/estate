package com.estate.sdzy.system.controller;

import com.estate.common.controller.BaseController;
import com.estate.util.UploadFileToFileServer;
import org.json.JSONException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@RestController
@CrossOrigin
@RequestMapping("/sdzy/ueditor")
public class UeditorController extends BaseController {

        @RequestMapping(value = "/exec")
        @ResponseBody
        public String exec(HttpServletRequest request,@RequestParam("image") MultipartFile file,@RequestParam("fileName") String fileName) throws UnsupportedEncodingException, JSONException {
            String path = null;
            UploadFileToFileServer server = new UploadFileToFileServer();
            try {
                path = server.fileUpload(file.getBytes(),fileName);
            }catch (Exception e){

            }
            return path;
        }
}
