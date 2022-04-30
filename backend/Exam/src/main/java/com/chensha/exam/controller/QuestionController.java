package com.chensha.exam.controller;

import com.chensha.exam.service.QuestionService;
import com.chensha.exam.vo.Result;
import com.chensha.exam.vo.params.QuestionParams;
import com.chensha.exam.vo.params.Uni2Params;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("ques")
public class QuestionController {
    @Autowired
    public QuestionService questionService;

    @GetMapping("listallques")
    public Result listAllQues(@RequestHeader("Authorization")String authHeader){
        return questionService.listAllQues(authHeader);
    }

    @PostMapping("addques")
    public Result addQues(@RequestBody QuestionParams questionParams, @RequestHeader("Authorization") String authHeader){
        return questionService.addQues(authHeader,questionParams);
    }

    @GetMapping("listques/{examid}")
    public Result listQues(@PathVariable(required = false,name = "examid") String examid,@RequestHeader("Authorization")String authHeader){
        return questionService.listQues(examid,authHeader);
    }

    @GetMapping("getquesbyid/{quesid}")
    public Result getQuesById(@PathVariable(required = false,name = "quesid") String quesid,@RequestHeader("Authorization")String authHeader){
        return questionService.getQuesById(quesid,authHeader);
    }

    @PostMapping("linkques")
    public Result linkQues(@RequestBody Uni2Params questionParams, @RequestHeader("Authorization") String authHeader){
        return questionService.linkQues(authHeader,questionParams);
    }

    @PostMapping("removeques")
    public Result removeQues(@RequestBody Uni2Params questionParams, @RequestHeader("Authorization") String authHeader){
        return questionService.removeQues(authHeader,questionParams);
    }
}
