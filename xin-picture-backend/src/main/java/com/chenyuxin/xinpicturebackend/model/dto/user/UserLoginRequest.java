package com.chenyuxin.xinpicturebackend.model.dto.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 用户登录请求
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 8735650154179439661L;

    /**
     * 账号
     */
    @NotBlank(message = "账号不能为空")
    private String userAccount;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String userPassword;
}
