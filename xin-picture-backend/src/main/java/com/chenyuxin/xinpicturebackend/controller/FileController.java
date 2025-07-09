package com.chenyuxin.xinpicturebackend.controller;

import com.chenyuxin.xinpicturebackend.common.BaseResponse;
import com.chenyuxin.xinpicturebackend.common.ResultUtils;
import com.chenyuxin.xinpicturebackend.exception.BusinessException;
import com.chenyuxin.xinpicturebackend.exception.ErrorCode;
import com.chenyuxin.xinpicturebackend.manager.CosManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author YuXin.Dev
 * @Date: 2025/7/8 09:01
 */
@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {
    @Autowired
    private CosManager cosManager;
    @PostMapping("/test/upload")
    public BaseResponse<String> testUpload(@RequestPart("file")MultipartFile multipartFile){
        String originalFilename = multipartFile.getOriginalFilename();
        String filePath = String.format("/test/%s", originalFilename);
        File tempFile = null;
        try {
            tempFile = File.createTempFile(filePath,null);
            multipartFile.transferTo(tempFile);
            cosManager.putObject(filePath,tempFile);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"上传测试图片失败");
        }finally {
            if (tempFile!=null){
                boolean delete = tempFile.delete();
                if(!delete){
                    log.error("删除临时文件失败,filePath:{}",filePath);
                }
            }
        }
        return ResultUtils.success(filePath);
    }
}
