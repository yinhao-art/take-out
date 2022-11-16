package com.hyh.reggie.controller;

import com.hyh.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传与下载
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {
    @Value("${reggie.path}")
    private String basePath;
    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) throws IOException {
        //file是临时文件,要转存到指定目录
        log.info(file.toString());
        //获取原始文件名
        String originalFilename = file.getOriginalFilename();
        //获取后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //使用uuid命名,防止文件名重复造成文件覆盖
        String fileName = UUID.randomUUID().toString()+suffix;

        //创建目录对象
        File dir=new File(basePath);
        if (!dir.exists()){
            dir.mkdir();
        }
        //将文件转入指定位置
        file.transferTo(new File(basePath+fileName));
        return R.success(fileName);
    }

    /**
     * 文件下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void downLoad(String name, HttpServletResponse response)  {
        //输入流,读取文件内容
        try{
            FileInputStream fileInputStream=new FileInputStream(new File(basePath+name));

            //输出流,将文件写回浏览器,在浏览器中展示图片
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            int length=0;
            byte[] bytes=new byte[1024];
            while ((length=fileInputStream.read(bytes)) !=-1){
            outputStream.write(bytes,0,length);
            outputStream.flush();
        }
        outputStream.close();
        fileInputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
