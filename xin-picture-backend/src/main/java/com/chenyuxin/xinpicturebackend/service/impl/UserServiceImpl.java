package com.chenyuxin.xinpicturebackend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenyuxin.xinpicturebackend.constant.UserConstant;
import com.chenyuxin.xinpicturebackend.exception.BusinessException;
import com.chenyuxin.xinpicturebackend.exception.ErrorCode;
import com.chenyuxin.xinpicturebackend.mapper.UserMapper;
import com.chenyuxin.xinpicturebackend.model.dto.user.UserQueryRequest;
import com.chenyuxin.xinpicturebackend.model.entity.User;
import com.chenyuxin.xinpicturebackend.model.enums.UserRoleEnum;
import com.chenyuxin.xinpicturebackend.model.vo.UserLoginVo;
import com.chenyuxin.xinpicturebackend.model.vo.UserVO;
import com.chenyuxin.xinpicturebackend.service.UserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author YuXin.Dev
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 用户注册
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param confirmPassword 确认密码
     * @return 用户id
     */
    @Override
    public Long userRegister(String userAccount, String userPassword, String confirmPassword){
        //1 参数校验
        if(!userPassword.equals(confirmPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"两次输入密码不一致");
        }
        //2 判断账号是否存在
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUserAccount,userAccount);
        Long count = this.baseMapper.selectCount(userLambdaQueryWrapper);
        if(count>0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号重复");
        }
        //3 加密密码
        String encryptPassword = this.getEncryptPassword(userPassword);
        //4 插入数据到数据库
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setUserName("新用户");
        user.setUserRole(UserRoleEnum.USER.getValue());
        boolean saveResult = this.save(user);
        if(!saveResult){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"注册失败，数据库错误");
        }
        return user.getId();
    }

    /**
     * 加密用户密码
     * @param userPassword 用户密码
     * @return 加密后的密码
     */
    @Override
    public String getEncryptPassword(String userPassword){
        final String SALT = "chenyuxin";
        return DigestUtils.md5DigestAsHex((SALT+userPassword).getBytes());
    }

    /**
     * 用户登录
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param request
     * @return 脱敏后的用户信息
     */
    @Override
    public UserLoginVo userLogin(String userAccount, String userPassword, HttpServletRequest request){
        //1 判断账号密码是否正确
        String encryptPassword = getEncryptPassword(userPassword);
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<User> wrapper = userLambdaQueryWrapper.eq(User::getUserAccount, userAccount)
                .eq(User::getUserPassword, encryptPassword);
        User user = this.baseMapper.selectOne(wrapper);
        if(user==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户名或密码不存在");
        }
        //2 保存用户登录状态
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE,user);
        return this.getUserLoginVo(user);
    }

    /**
     * 获取用户脱敏信息
     * @param user 用户信息
     * @return 脱敏后的用户信息
     */
    @Override
    public UserLoginVo getUserLoginVo(User user){
        if(user==null){
            return null;
        }
        return BeanUtil.copyProperties(user, UserLoginVo.class);
    }

    /**
     * 获取登录用户的信息
     * @param request
     * @return 用户信息
     */
    @Override
    public User getLoginUser(HttpServletRequest request){
        //1 判断用户是否登录
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if(currentUser==null||currentUser.getId()==null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        //2 从数据库查询用户信息返回
        currentUser = this.getById(currentUser.getId());
        if(currentUser==null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    /**
     * 用户退出登录
     * @param request
     * @return boolean
     */
    @Override
    public Boolean userLogout(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if(userObj == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
        return true;
    }

    /**
     * 获取分页查询对象
     * @param userQueryRequest 查询参数
     * @return wrapper
     */
    @Override
    public LambdaQueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest){
        Long id = userQueryRequest.getId();
        String userName = userQueryRequest.getUserName();
        String userAccount = userQueryRequest.getUserAccount();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(ObjUtil.isNotNull(id), User::getId, id);
        userLambdaQueryWrapper.eq(StrUtil.isNotBlank(userRole),User::getUserRole, userRole);
        userLambdaQueryWrapper.like(StrUtil.isNotBlank(userAccount), User::getUserAccount, userAccount);
        userLambdaQueryWrapper.like(StrUtil.isNotBlank(userName), User::getUserName, userName);
        userLambdaQueryWrapper.like(StrUtil.isNotBlank(userProfile), User::getUserProfile, userProfile);
        if (StrUtil.isNotBlank(sortField)) {
            boolean isAsc = "ascend".equals(sortOrder); // 安全的写法
            switch (sortField) {
                case "id":
                    userLambdaQueryWrapper.orderBy(true, isAsc, User::getId);
                    break;
                case "userName":
                    userLambdaQueryWrapper.orderBy(true, isAsc, User::getUserName);
                    break;
                case "userAccount":
                    userLambdaQueryWrapper.orderBy(true, isAsc, User::getUserAccount);
                    break;
                default:
                    userLambdaQueryWrapper.orderBy(true, false, User::getCreateTime);
                    break;
            }
        }
        return userLambdaQueryWrapper;
    }
    /**
     * 获取脱敏后的用户列表
     * @param userList
     * @return UserVo
     */
    @Override
    public List<UserVO> getUserVOList(List<User> userList) {
        if (CollUtil.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream()
                .map(this::getUserVO)
                .collect(Collectors.toList());
    }
    /**
     * 获得脱敏后的用户信息
     * @param user
     * @return List<UserVo>
     */
    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtil.copyProperties(user, userVO);
        return userVO;
    }
}
