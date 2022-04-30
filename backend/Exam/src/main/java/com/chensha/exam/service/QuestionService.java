package com.chensha.exam.service;

import com.chensha.exam.vo.Result;
import com.chensha.exam.vo.params.QuestionParams;
import com.chensha.exam.vo.params.Uni2Params;

public interface QuestionService {
    Result listAllQues(String authHeader);

    Result addQues(String authHeader, QuestionParams questionParams);

    int getQuesTypeById(String id);

    Result listQues(String examid, String authHeader);

    Result getQuesById(String quesid, String authHeader);

    Result linkQues(String authHeader, Uni2Params questionParams);

    Result removeQues(String authHeader, Uni2Params questionParams);

    Integer countQues(String examid);

    Float getFullScore(String examid);
}
