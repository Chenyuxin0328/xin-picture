package com.chenyuxin.xinpicturebackend.model.dto.user;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 更新用户请求
 */
@Data
public class UserUpdateRequest implements Serializable {

    /**
     * id
     */
    @NonNull
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 简介
     */
    private String userProfile;

    /**
     * 用户角色：user/admin
     */
    @Pattern(regexp = "^(user|admin)$", message = "用户角色只能是 user 或 admin")
    @NotBlank(message = "用户角色不能为空")
    private String userRole;

    private static final long serialVersionUID = 1L;
}