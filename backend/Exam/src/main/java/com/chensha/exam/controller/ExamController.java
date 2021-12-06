package com.chensha.exam.controller;

import com.chensha.exam.service.ExamService;
import com.chensha.exam.utils.JWTUtils;
import com.chensha.exam.vo.Result;
import com.chensha.exam.vo.params.ExamParams;
import com.chensha.exam.vo.params.LoginParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("exam")
public class ExamController {

    @Autowired
    private ExamService examService;

    @GetMapping("listexam")
    public Result listExam(@RequestHeader("Authorization")String authHeader) {
        return examService.listExam(authHeader);
    }

    @PostMapping("addexam")
    public Result addExam(@RequestBody ExamParams examParams,@RequestHeader(JWTUtils.AUTH_HEADER_KEY) String authHeader){
        return examService.addExam(authHeader,examParams);
    }
}
