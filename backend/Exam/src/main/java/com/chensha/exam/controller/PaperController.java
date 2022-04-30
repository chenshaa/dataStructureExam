package com.chensha.exam.controller;

import com.chensha.exam.service.PaperService;
import com.chensha.exam.vo.Result;
import com.chensha.exam.vo.params.PaperParams;
import com.chensha.exam.vo.params.UpdateQuesParams;
import com.chensha.exam.vo.params.UpdateScore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("paper")
public class PaperController {
    @Autowired
    public PaperService paperService;

    @PostMapping("addpaper")
    public Result addPaper(@RequestBody PaperParams paperParams, @RequestHeader("Authorization")String authHeader){
        return paperService.addPaper(paperParams, authHeader);
    }

    @PostMapping("getpaper/{examid}")
    public Result getPaper(@PathVariable("examid") String examId,@RequestHeader("Authorization")String authHeader){
        return paperService.getPaper(examId, authHeader);
    }

    @PostMapping("updateone")
    public Result updateOne(@RequestBody UpdateQuesParams updateQuesParams, @RequestHeader("Authorization")String authHeader){
        return paperService.updateOne(updateQuesParams, authHeader);
    }

    @GetMapping("listmyexam")
    public Result listMyExam(@RequestHeader("Authorization")String authHeader) {
        return paperService.listMyExam(authHeader);
    }

    @GetMapping("startexam/{examid}")
    public Result startExam(@PathVariable("examid") String examId,@RequestHeader("Authorization")String authHeader){
        return paperService.startExam(examId, authHeader);
    }

    @GetMapping(value = {"/autocorrect/{examid}","/autocorrect" })
    public Result autoCorrect(@PathVariable(required = false,name = "examid") String examId,@RequestHeader("Authorization") String authHeader){
        return paperService.autoCorrect(examId,authHeader);
    }

    @GetMapping("liststu/{examid}")
    public Result listStu(@PathVariable("examid") String examId,@RequestHeader("Authorization")String authHeader){
        return paperService.listStu(examId, authHeader);
    }

    @GetMapping("correctProgress/{examid}")
    public Result correctProgress(@PathVariable("examid") String examId,@RequestHeader("Authorization")String authHeader){
        return paperService.correctProgress(examId, authHeader);
    }

    @GetMapping("quesCorrectProgress/{examid}/{quesid}")
    public Result quesCorrectProgress(@PathVariable("examid") String examId,@PathVariable("quesid") String quesId,@RequestHeader("Authorization")String authHeader){
        return paperService.quesCorrectProgress(quesId, examId,authHeader);
    }

    @GetMapping("getAnswerList/{examid}/{quesid}")
    public Result getAnswerList(@PathVariable("examid") String examId,@PathVariable("quesid") String quesId,@RequestHeader("Authorization")String authHeader){
        return paperService.getAnswerList(quesId, examId,authHeader);
    }

    @PostMapping("updatescore")
    public Result updateScore(@RequestBody UpdateScore UpdateScore, @RequestHeader("Authorization")String authHeader){
        return paperService.updateScore(UpdateScore, authHeader);
    }

    @GetMapping("calculateScore/{examid}")
    public Result calculateScore(@PathVariable("examid") String examId,@RequestHeader("Authorization")String authHeader){
        return paperService.calculateScore(examId, authHeader);
    }

}
