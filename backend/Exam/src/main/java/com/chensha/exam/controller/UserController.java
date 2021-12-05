package com.chensha.exam.controller;

import com.chensha.exam.service.UserService;
import com.chensha.exam.vo.Result;
import com.chensha.exam.vo.params.LoginParams;
import com.chensha.exam.vo.params.UserParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("login")
    public Result login(@RequestBody LoginParams loginParams){
        return userService.login(loginParams);
    }

    @PostMapping("adduser")
    public Result addUser(@RequestBody UserParams userParams){
        return userService.addUser(userParams);
    }
}
