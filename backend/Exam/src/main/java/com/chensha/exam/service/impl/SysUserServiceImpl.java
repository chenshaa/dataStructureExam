package com.chensha.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chensha.exam.dao.mapper.UserMapper;
import com.chensha.exam.dao.pojo.User;
import com.chensha.exam.service.SysUserService;
import com.chensha.exam.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public User findUser(String account, String password) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserAccount,account);
        queryWrapper.eq(User::getUserPassword,password);
        queryWrapper.select(User::getUserAccount,User::getUserNickname);
        queryWrapper.last("limit 1");

        return (userMapper.selectOne(queryWrapper));
    }

    @Override
    public User getUserByAccount(String account){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserAccount,account);
        queryWrapper.last("limit 1");

        return (userMapper.selectOne(queryWrapper));
    }

    @Override
    public void save(User user) {
        this.userMapper.insert(user);
    }

    @Override
    public int getGroupByAccount(String account) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserAccount,account);
        queryWrapper.last("limit 1");

        return (userMapper.selectOne(queryWrapper).getUserGroup());
    }

    @Override
    public String authToken(String authHeader) {
        String token = authHeader.substring(7);
        String account = JWTUtils.getAccount(token);
        int userGroup = getGroupByAccount(account);
        if (userGroup != 0 && userGroup != 1) {
            return null;
        }
        return token;
    }
}
