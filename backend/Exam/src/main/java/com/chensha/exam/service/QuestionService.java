package com.chensha.exam.service;

import com.chensha.exam.vo.Result;
import com.chensha.exam.vo.params.QuestionParams;
import com.chensha.exam.vo.params.LinkQuesParams;

public interface QuestionService {
    Result listAllQues(String authHeader);

    Result addQues(String authHeader, QuestionParams questionParams);

    int getQuesTypeById(String id);

    Result listQues(String examid, String authHeader);

    Result getQuesById(String quesid, String authHeader);

    Result linkQues(String authHeader, LinkQuesParams questionParams);

    Result disLinkQues(String authHeader, LinkQuesParams questionParams);

    Integer countQues(String examid);

    Float getFullScore(String examid);
}
