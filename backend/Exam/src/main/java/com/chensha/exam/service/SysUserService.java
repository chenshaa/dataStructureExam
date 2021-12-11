package com.chensha.exam.service;

import com.chensha.exam.dao.pojo.User;

public interface SysUserService {

    User findUser(String account, String password);

    User getUserByAccount(String account);

    void save(User user);

    int getGroupByAccount(String account);

    public String authTokenAdmin(String authHeader);

    String getIdByAccount(String account);
}
