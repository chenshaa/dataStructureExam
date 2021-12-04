package com.chensha.exam.controller;

import com.chensha.exam.service.UserService;
import com.chensha.exam.vo.Result;
import com.chensha.exam.vo.UserLogin;
import com.chensha.exam.vo.params.UserLoginParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("login")
    public Result login(@RequestBody UserLoginParams userLoginParams){
        Result result= userService.login(userLoginParams);
        return result;
    }
}
