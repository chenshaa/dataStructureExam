package com.chensha.exam.controller;

import com.chensha.exam.vo.Result;
import com.chensha.exam.vo.params.LoginParams;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("exam")
public class ExamController {

    @PostMapping("test")
    public Result test(@RequestHeader("Authorization")String token, @RequestBody LoginParams loginParams){
        return Result.success("成功");
    }
}
