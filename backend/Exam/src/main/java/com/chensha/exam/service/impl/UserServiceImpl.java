package com.chensha.exam.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.chensha.exam.dao.pojo.User;
import com.chensha.exam.service.SysUserService;
import com.chensha.exam.service.UserService;
import com.chensha.exam.utils.JWTUtils;
import com.chensha.exam.vo.ErrorCode;
import com.chensha.exam.vo.Result;
import com.chensha.exam.vo.UserLogin;
import com.chensha.exam.vo.params.LoginParams;
import com.chensha.exam.vo.params.UserParams;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private static final String slat = "chensha";

    @Autowired
    private SysUserService sysUserService;

    @Override
    public Result login(LoginParams loginParams) {
        String account= loginParams.getAccount();
        String password= loginParams.getPassword();

        if(StringUtils.isBlank(account) || StringUtils.isBlank(password)){
            return Result.fail(ErrorCode.ERROR_PARAMETER.getCode(),ErrorCode.ERROR_PARAMETER.getMsg());
        }

        password= DigestUtils.md5Hex(password + slat);
        User user=sysUserService.findUser(account,password);

        if (user == null){
            return Result.fail(ErrorCode.ERROR_USERNAME.getCode(),ErrorCode.ERROR_USERNAME.getMsg());
        }
        String token= JWTUtils.createToken(user.getUserAccount());

        UserLogin userLogin = new UserLogin();
        userLogin.setAccount(user.getUserAccount());
        userLogin.setNickname(user.getUserNickname());
        userLogin.setToken(token);
        return Result.success(userLogin);
    }

    @Override
    public Result addUser(UserParams userParams,String authHeader) {

        if(sysUserService.authTokenAdmin(authHeader)==null){
            return Result.fail(ErrorCode.NO_PERMISSION.getCode(), ErrorCode.NO_PERMISSION.getMsg());
        }

        String account = userParams.getAccount();
        String userNickname = userParams.getUserNickname();
        String password = userParams.getPassword();
        Integer userGroup = userParams.getUserGroup();

        if(StringUtils.isBlank(account) || StringUtils.isBlank(userNickname) || StringUtils.isBlank(password) || userGroup == null){
            return Result.fail(ErrorCode.ERROR_PARAMETER.getCode(), ErrorCode.ERROR_PARAMETER.getMsg());
        }

        if(sysUserService.getUserByAccount(account) != null){
            return Result.fail(ErrorCode.OBJECT_EXISTS.getCode(), ErrorCode.OBJECT_EXISTS.getMsg());
        }

        User user = new User();
        user.setUserAccount(account);
        user.setUserNickname(userNickname);
        user.setUserPassword(DigestUtils.md5Hex(password + slat));
        user.setUserGroup(userGroup);
        user.setUserCreateTime(System.currentTimeMillis());
        user.setUserUpdateTime(System.currentTimeMillis());
        this.sysUserService.save(user);

        return Result.success("成功");
    }

    @Override
    public User checkToken(String token) {
        if (org.apache.commons.lang3.StringUtils.isBlank(token)){
            return null;
        }
        Map<String, Object> stringObjectMap = JWTUtils.checkToken(token);
        if (stringObjectMap == null){
            return null;
        }

        User user = new User();
        user.setUserAccount(JWTUtils.getAccount(token));
        return user;
    }

    @Override
    public Result listStu(String authHeader) {
        if(sysUserService.authTokenAdmin(authHeader)==null){
            return Result.fail(ErrorCode.NO_PERMISSION.getCode(), ErrorCode.NO_PERMISSION.getMsg());
        }

        return Result.success(sysUserService.listStu());
    }
}
