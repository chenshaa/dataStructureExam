package com.chensha.exam.service;

import com.chensha.exam.vo.Result;
import com.chensha.exam.vo.params.PaperParams;
import com.chensha.exam.vo.params.UpdateQuesParams;

public interface PaperService {
    Result listMyExam(String authHeader);

    Result startExam(String examId, String authHeader);

    Result addPaper(PaperParams paperParams, String authHeader);

    Result getPaper(String examId, String authHeader);

    Result updateOne(UpdateQuesParams updateQuesParams, String authHeader);
}
