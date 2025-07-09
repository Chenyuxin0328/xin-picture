package com.chenyuxin.xinpicturebackend.controller;

import com.chenyuxin.xinpicturebackend.common.BaseResponse;
import com.chenyuxin.xinpicturebackend.common.ResultUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class TestController {
    @GetMapping("/health")
    public BaseResponse<String> health(){
        return ResultUtils.success("health");
    }
}
