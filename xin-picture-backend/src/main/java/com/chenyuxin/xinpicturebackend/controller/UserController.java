package com.chenyuxin.xinpicturebackend.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chenyuxin.xinpicturebackend.annotation.AuthCheck;
import com.chenyuxin.xinpicturebackend.common.BaseResponse;
import com.chenyuxin.xinpicturebackend.common.ResultUtils;
import com.chenyuxin.xinpicturebackend.constant.UserConstant;
import com.chenyuxin.xinpicturebackend.exception.BusinessException;
import com.chenyuxin.xinpicturebackend.exception.ErrorCode;
import com.chenyuxin.xinpicturebackend.exception.ThrowUtils;
import com.chenyuxin.xinpicturebackend.model.dto.user.*;
import com.chenyuxin.xinpicturebackend.model.entity.User;
import com.chenyuxin.xinpicturebackend.model.vo.UserLoginVo;
import com.chenyuxin.xinpicturebackend.model.vo.UserVO;
import com.chenyuxin.xinpicturebackend.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 注册接口
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody @Valid UserRegisterRequest userRegisterRequest){
        ThrowUtils.throwIf(userRegisterRequest==null, ErrorCode.PARAMS_ERROR,"参数为空");
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        Long userId = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(userId);
    }

    /**
     * 登录接口
     */
    @PostMapping("/login")
    public BaseResponse<UserLoginVo> userLogin(@RequestBody @Valid UserLoginRequest userLoginRequest,
                                               HttpServletRequest request){
        ThrowUtils.throwIf(userLoginRequest==null,ErrorCode.PARAMS_ERROR,"参数为空");
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        UserLoginVo userLoginVo = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(userLoginVo);
    }

    /**
     * 获取登录用户信息
     */
    @GetMapping("/get/login")
    public BaseResponse<UserLoginVo> getLoginUser(HttpServletRequest request){
        User loginUser = userService.getLoginUser(request);
        return ResultUtils.success(userService.getUserLoginVo(loginUser));
    }

    /**
     * 用户退出登录
     */
    @GetMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request){
        Boolean bool = userService.userLogout(request);
        return ResultUtils.success(bool);
    }

    /**
     * 创建用户
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUser(@RequestBody @Valid UserAddRequest userAddRequest) {
        ThrowUtils.throwIf(userAddRequest == null, ErrorCode.PARAMS_ERROR);
        User user = new User();
        BeanUtil.copyProperties(userAddRequest, user);
        // 默认密码
        final String DEFAULT_PASSWORD = "12345678";
        String encryptPassword = userService.getEncryptPassword(DEFAULT_PASSWORD);
        user.setUserPassword(encryptPassword);
        // 插入数据库
        boolean result = userService.save(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(user.getId());
    }
    /**
     * 根据 id 获取用户（仅管理员）
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<User> getUserById(long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(user);
    }
    /**
     * 根据 id 获取包装类
     */
    @GetMapping("/get/vo")
    public BaseResponse<UserVO> getUserVOById(long id) {
        BaseResponse<User> response = getUserById(id);
        User user = response.getData();
        return ResultUtils.success(userService.getUserVO(user));
    }
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        if (userUpdateRequest == null || userUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest, user);
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }
    /**
     * 分页查询用户信息
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<UserVO>> selectPageUser(@RequestBody UserQueryRequest userQueryRequest){
        ThrowUtils.throwIf(userQueryRequest==null,ErrorCode.PARAMS_ERROR,"请求参数为空");
        int current = userQueryRequest.getCurrent();
        int pageSize = userQueryRequest.getPageSize();
        Page<User> userPage = userService.page(new Page<>(current, pageSize), userService.getQueryWrapper(userQueryRequest));
        Page<UserVO> userVoPage = new Page<>(current, pageSize, userPage.getTotal());
        List<UserVO> userVOList = userService.getUserVOList(userPage.getRecords());
        userVoPage.setRecords(userVOList);
        return ResultUtils.success(userVoPage);
    }


}
