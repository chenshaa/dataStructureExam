package com.chensha.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.chensha.exam.dao.mapper.UserMapper;
import com.chensha.exam.dao.pojo.User;
import com.chensha.exam.service.UserService;
import com.chensha.exam.utils.JWTUtils;
import com.chensha.exam.vo.Result;
import com.chensha.exam.vo.UserLogin;
import com.chensha.exam.vo.params.UserLoginParams;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private static final String slat = "chensha";

    @Autowired
    private UserMapper userMapper;

    @Override
    public Result login(UserLoginParams userLoginParams) {
        String account=userLoginParams.getAccount();
        String password=userLoginParams.getPassword();

        if(StringUtils.isBlank(account) || StringUtils.isBlank(password)){
            return Result.fail(400,"账号或密码为空");
        }

        password= DigestUtils.md5Hex(password + slat);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserAccount,account);
        queryWrapper.eq(User::getUserPassword,password);
        queryWrapper.select(User::getUserAccount,User::getUserNickname);
        queryWrapper.last("limit 1");

        User user=userMapper.selectOne(queryWrapper);
        if (user == null){
            return Result.fail(401,password+"账号或密码错误");
        }
        String token= JWTUtils.createToken(user.getUserAccount());

        UserLogin userLogin = new UserLogin();
        userLogin.setAccount(user.getUserAccount());
        userLogin.setNickname(user.getUserNickname());
        userLogin.setToken(token);
        return Result.success(userLogin);
    }
}
