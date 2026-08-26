package com.sky.controller.admin;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 通用接口
 * @author tyq
 * @date 2026年8月25日
 * @project_name sky-server
 * @package_name com.sky.controller.admin
 * @file_name CommonController.java
 * @classname CommonController
 * @version 2026年8月25日 上午1:45:58
 */
@RestController
@RequestMapping("/admin/common")
@Slf4j
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;
    
    /**
     * 文件上传
     * @param file
     * @return
     */
	@PostMapping("/upload")
    public Result<String> upload(MultipartFile file) {
		//获取原始文件名
        String originalName = file.getOriginalFilename();
        //截取后缀
        String suffix = originalName.substring(originalName.lastIndexOf("."));
        //使用UUID生成新的文件名
        String newFileName = UUID.randomUUID().toString() + suffix;
        //上传到阿里云
		try {
            String filePath = aliOssUtil.upload(file.getBytes(), newFileName);
            return Result.success(filePath);
		} catch (IOException e) {
			log.error("文件上传失败:{}", e.getMessage());
		}
		return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
