package com.chenyuxin.xinpicturebackend.model.dto.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 用户创建请求
 */
@Data
public class UserAddRequest implements Serializable {

    private static final long serialVersionUID = -8755841759798202604L;
    /**
     * 用户昵称
     */
    private String userName = "新用户";

    /**
     * 账号
     */
    @NotBlank(message = "账号不能为空")
    @Size(min = 8,message = "账号最短为8位数")
    private String userAccount;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 用户角色: user, admin
     */
    @Pattern(regexp = "^(user|admin)$", message = "用户角色只能是 user 或 admin")
    @NotBlank(message = "用户角色不能为空")
    private String userRole;

}