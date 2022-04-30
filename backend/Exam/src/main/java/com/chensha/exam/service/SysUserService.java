package com.chensha.exam.service;

import com.chensha.exam.dao.pojo.User;
import com.chensha.exam.vo.UserVo;

import java.util.List;

public interface SysUserService {

    User findUser(String account, String password);

    User getUserByAccount(String account);

    void save(User user);

    int getGroupByAccount(String account);

    String authTokenAdmin(String authHeader);

    String getIdByAccount(String account);

    Object listStu();

    boolean authToken(String authHeader);

    User getUserById(String userId);

    List<UserVo> copyList(List<User> userList);
}
