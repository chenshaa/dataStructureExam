package com.chensha.exam.controller;

import com.chensha.exam.service.ExamService;
import com.chensha.exam.vo.Result;
import com.chensha.exam.vo.params.ExamParams;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exam")
public class ExamController {

    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @GetMapping("/listexam")
    public Result listExam(@RequestHeader("Authorization") String authHeader) {
        return examService.listExam(authHeader);
    }

    @PostMapping("/addexam")
    public Result addExam(@RequestBody ExamParams examParams, @RequestHeader("Authorization") String authHeader) {
        return examService.addExam(authHeader, examParams);
    }

    @GetMapping(value = {"/setexamend/{examid}/{timestamp}", "/setexamend/{examid}", "/setexamend"})
    public Result setExamEnd(@PathVariable(required = false, name = "examid") String examid, @PathVariable(required = false, name = "timestamp") Long timestamp
            , @RequestHeader("Authorization") String authHeader) {
        return examService.setExamEnd(examid, timestamp, authHeader);
    }

    @GetMapping(value = {"/setexamstart/{examid}/{timestamp}", "/setexamstart/{examid}", "/setexamstart"})
    public Result setExamStart(@PathVariable(required = false, name = "examid") String examid, @PathVariable(required = false, name = "timestamp") Long timestamp
            , @RequestHeader("Authorization") String authHeader) {
        return examService.setExamStart(examid, timestamp, authHeader);
    }


}
