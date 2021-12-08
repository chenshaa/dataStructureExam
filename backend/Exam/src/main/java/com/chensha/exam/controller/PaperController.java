package com.chensha.exam.controller;

import com.chensha.exam.service.PaperService;
import com.chensha.exam.vo.Result;
import com.chensha.exam.vo.params.PaperParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("paper")
public class PaperController {
    @Autowired
    public PaperService paperService;

    @GetMapping("listmyexam")
    public Result listMyExam(@RequestHeader("Authorization")String authHeader) {
        return paperService.listMyExam(authHeader);
    }

    @GetMapping("Startexam/{examid}")
    public Result startExam(@PathVariable("examid") String examId,@RequestHeader("Authorization")String authHeader){
        return paperService.startExam(examId, authHeader);
    }

    @PostMapping("addpaper")
    public Result addPaper(@RequestBody PaperParams paperParams, @RequestHeader("Authorization")String authHeader){
        return paperService.addPaper(paperParams, authHeader);
    }
}
