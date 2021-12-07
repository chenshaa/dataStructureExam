package com.chensha.exam.service;

import com.chensha.exam.vo.Result;
import com.chensha.exam.vo.params.QuestionParams;

public interface QuestionService {
    Result listQues(String authHeader);

    Result addQues(String authHeader, QuestionParams questionParams);
}
