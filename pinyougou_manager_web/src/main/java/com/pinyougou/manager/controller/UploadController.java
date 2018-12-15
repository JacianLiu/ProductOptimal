package com.pinyougou.manager.controller;

import entity.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import utils.FastDFSClient;

/**
 * @author Ricky
 * @version 1.0
 * @description TODO
 * @date 2018/12/12
 */
@RestController
@RequestMapping("/upload")
public class UploadController {

    @Value("${FILE_SERVER_URL}")
    private String fileServerUrl;

    /**
     * 基于FastDFS工具类实现文件上传功能
     * @param file SpringMVC 文件上传对象
     * @return 执行结果
     */
    @RequestMapping("/uploadFile")
    public Result uploadFile(MultipartFile file) {
        try {
            // 获取文件名
            String filename = file.getOriginalFilename();
            // 获取文件扩展名
            String substring = filename.substring(filename.lastIndexOf(".") + 1);
            // 调用工具类实现文件上传
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:config/fdfs_client.conf");
            // 文件存储目录+文件名
            String filePath = fastDFSClient.uploadFile(file.getBytes(), substring);
            String fileUrl = fileServerUrl + filePath;
            return new Result(true, fileUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "图片上传失败");
        }
    }
}
