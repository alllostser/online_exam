package com.exam.controller;

import com.alibaba.fastjson.JSONObject;
import com.exam.commons.ServerResponse;
import com.exam.utils.FTPUtil;
import com.exam.utils.PropertiesUtil;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName UploadContorller
 * @Description //图片上传
 * @Author GuXinYu
 * @Date 2020/5/30 21:10
 * @Version 1.0
 **/
@RestController
public class UploadContorller {

    @RequestMapping(value = "/do_upload.do",method = RequestMethod.POST)
    public ServerResponse doUpload(@RequestParam("pic") MultipartFile file){
        if (file.isEmpty()){
            return ServerResponse.serverResponseByFail(500,"上传文件不能为空");}
        //step1:获取文件的名称
        String filename = file.getOriginalFilename();
        //step2:获取原文件的扩展名
        String ext = filename.substring(filename.lastIndexOf("."));
        //step3:定义新的文件名,为文件生产一个唯一名称
        String newName = UUID.randomUUID().toString();
        String newFilename = newName+System.currentTimeMillis()+file.getOriginalFilename();
        try {
            //step5:创建文件
            File newFile = new File(PropertiesUtil.getProperty("imageHost")+PropertiesUtil.getProperty("upload"),newFilename);
            newFile.mkdirs();
            file.transferTo(newFile);

            //把file上传到ftp服务器
            List<File> files = new ArrayList<>();
            files.add(newFile);
            boolean result = FTPUtil.uploadFile("/uploadImage", files);
            if (!result){
                return ServerResponse.serverResponseByFail(500,"上传Ftp服务器失败");
            }
            newFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ServerResponse.serverResponseBySucess("上传成功");
    }
}
