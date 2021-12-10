package com.chensha.exam.service;

import com.chensha.exam.vo.Result;
import com.chensha.exam.vo.params.ExamParams;

public interface ExamService {
    Result listExam(String authHeader);

    Result addExam(String authHeader, ExamParams examParams);

    Result endExam(String examId,Long timestamp, String authHeader);
}
