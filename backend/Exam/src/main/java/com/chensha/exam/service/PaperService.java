package com.chensha.exam.service;

import com.chensha.exam.vo.Result;
import com.chensha.exam.vo.params.PaperParams;
import com.chensha.exam.vo.params.UpdateQuesParams;
import com.chensha.exam.vo.params.UpdateScore;

public interface PaperService {
    Result listMyExam(String authHeader);

    Result startExam(String examId, String authHeader);

    Result addPaper(PaperParams paperParams, String authHeader);

    Result getPaper(String examId, String authHeader);

    Result updateOne(UpdateQuesParams updateQuesParams, String authHeader);

    Result autoCorrect(String examId, String authHeader);

    Result listStu(String examId, String authHeader);

    Result correctProgress(String examId, String authHeader);

    Result quesCorrectProgress(String quesId,String examId, String authHeader);

    Result getAnswerList(String quesId, String examId, String authHeader);

    Result updateScore(UpdateScore updateScore, String authHeader);

    Result calculateScore(String examId, String authHeader);
}
