package com.chensha.exam.service;

import com.chensha.exam.vo.Result;
import com.chensha.exam.vo.UserLogin;
import com.chensha.exam.vo.params.UserLoginParams;

public interface UserService {
    Result login(UserLoginParams userLoginParams);
}
