package com.chenyuxin.xinpicturebackend.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chenyuxin.xinpicturebackend.model.dto.user.UserModifyRequest;
import com.chenyuxin.xinpicturebackend.model.dto.user.UserQueryRequest;
import com.chenyuxin.xinpicturebackend.model.entity.User;
import com.chenyuxin.xinpicturebackend.model.vo.UserLoginVo;
import com.chenyuxin.xinpicturebackend.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author YuXin.Dev
 */
public interface UserService extends IService<User> {
    /**
     * 用户注册
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param confirmPassword 确认密码
     * @return 用户id
     */
    Long userRegister(String userAccount, String userPassword, String confirmPassword);
    /**
     * 加密用户密码
     * @param userPassword 用户密码
     * @return 加密后的密码
     */
    String getEncryptPassword(String userPassword);
    /**
     * 用户登录
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param request
     * @return 脱敏后的用户信息
     */
    UserLoginVo userLogin(String userAccount, String userPassword, HttpServletRequest request);
    /**
     * 获取用户脱敏信息
     * @param user 用户信息
     * @return 脱敏后的用户信息
     */
    UserLoginVo getUserLoginVo(User user);

    /**
     * 获取登录用户的信息
     * @param request
     * @return 用户信息
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 用户退出登录
     * @param request
     * @return boolean
     */
    Boolean userLogout(HttpServletRequest request);

    /**
     * 获取分页查询对象
     * @param userQueryRequest 查询参数
     * @return wrapper
     */
    LambdaQueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

    List<UserVO> getUserVOList(List<User> userList);

    UserVO getUserVO(User user);
    /**
     * 修改用户信息
     * @param userModifyRequest
     * @return Boolean
     */
    Boolean modifyUser(UserModifyRequest userModifyRequest);
    /**
     * 删除用户
     * @param userId 用户id
     * @return Boolean
     */
    Boolean deleteUser(Long userId);
}
