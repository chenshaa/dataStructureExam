package com.chensha.exam.controller;

import com.chensha.exam.service.QuestionService;
import com.chensha.exam.utils.JWTUtils;
import com.chensha.exam.vo.Result;
import com.chensha.exam.vo.params.ExamParams;
import com.chensha.exam.vo.params.QuestionParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("ques")
public class QuestionController {
    @Autowired
    public QuestionService questionService;

    @GetMapping("listques")
    public Result listQues(@RequestHeader("Authorization")String authHeader){
        return questionService.listQues(authHeader);
    }

    @PostMapping("addques")
    public Result addQues(@RequestBody QuestionParams questionParams, @RequestHeader(JWTUtils.AUTH_HEADER_KEY) String authHeader){
        return questionService.addQues(authHeader,questionParams);
    }
}
