package com.chensha.exam.service;

import com.chensha.exam.dao.pojo.User;
import com.chensha.exam.vo.Result;
import com.chensha.exam.vo.params.LoginParams;
import com.chensha.exam.vo.params.UserParams;

public interface UserService {
    Result login(LoginParams loginParams);

    Result addUser(UserParams userParams);

    User checkToken(String token);
}
