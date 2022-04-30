package com.chensha.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chensha.exam.dao.mapper.UserMapper;
import com.chensha.exam.dao.pojo.User;
import com.chensha.exam.service.SysUserService;
import com.chensha.exam.utils.JWTUtils;
import com.chensha.exam.vo.Result;
import com.chensha.exam.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


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
    public String authTokenAdmin(String authHeader) {
        String token = authHeader.substring(7);
        String account = JWTUtils.getAccount(token);
        int userGroup = getGroupByAccount(account);
        if (userGroup != 0 && userGroup != 1) {
            return null;
        }
        return token;
    }

    @Override
    public String getIdByAccount(String account) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserAccount,account);
        queryWrapper.last("limit 1");

        return (userMapper.selectOne(queryWrapper).getUserId());
    }

    @Override
    public Object listStu() {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserGroup,2);
        List<User> stuList = userMapper.selectList(queryWrapper);
        return Result.success(copyList(stuList));
    }

    @Override
    public boolean authToken(String authHeader) {
        String token = authHeader.substring(7);
        String account = JWTUtils.getAccount(token);
        if(account!=null){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public User getUserById(String userId) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserId,userId);

        return (userMapper.selectOne(queryWrapper));
    }

    public List<UserVo> copyList(List<User> userList){
        List<UserVo> userVoList = new ArrayList<>();
        for (User user : userList) {
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(user, userVo);
            userVoList.add(userVo);
        }
        return userVoList;
    }
}
