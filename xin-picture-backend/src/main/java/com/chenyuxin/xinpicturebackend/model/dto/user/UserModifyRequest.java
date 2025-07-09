package com.chenyuxin.xinpicturebackend.model.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author YuXin.Dev
 * @Date: 2025/7/8 17:17
 */
@Data
@NoArgsConstructor
public class UserModifyRequest implements Serializable {
    @NotNull(message = "id为空")
    private Long id;

    /**
     * 用户昵称
     */
    @NotBlank(message = "用户昵称为空")
    private String userName;

    /**
     * 用户头像
     */
    @NotBlank(message = "用户头像为空")
    private String userAvatar;

    /**
     * 简介
     */
    private String userProfile;
    private static final long serialVersionUID = -7687309458551229969L;
}
