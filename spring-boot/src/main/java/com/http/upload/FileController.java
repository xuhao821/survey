package com.http.upload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;

/**
 *
 * @ClassName: FileController
 * @Description: 文件上传
 * @Author: Ian
 * @Date: 2018/3/14 14:34
 * @Version: 1.0
 */
@RestController
@RequestMapping("file")
public class FileController {

//    @Autowired
//    HttpServletRequest request;

    @GetMapping("test")
    public String test(){
        return "test";
    }

    @PostMapping("uploadImg")
    public String uploadImg(@RequestParam("file") MultipartFile file, HttpServletRequest request){
        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();
         System.out.println("fileName-->" + fileName);
        System.out.println("getContentType-->" + contentType);
        String filePath = request.getSession().getServletContext().getRealPath("imgupload/");
        try {
            FileController.uploadFile(file.getBytes(), "c:/imgupload/", fileName);
        } catch (Exception e) {
            // TODO: handle exception
        }
        //返回json
        return "uploadimg success";
    }

    public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath+fileName);
        out.write(file);
        out.flush();
        out.close();
    }
}
