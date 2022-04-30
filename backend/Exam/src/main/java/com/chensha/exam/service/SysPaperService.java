package com.chensha.exam.service;

import com.chensha.exam.dao.pojo.Exam;
import com.chensha.exam.dao.pojo.Paper;
import com.chensha.exam.vo.ExamVo;
import com.chensha.exam.vo.QuestionLiteVo;

import java.util.List;

public interface SysPaperService {
    ExamVo getExamVoById(String id);

    Exam getExamById(String id);

    Paper getPaperByExamId(String examId, String owner);

    List<QuestionLiteVo> getQuesLiteVoByExamId(String examId);

    String getPaperIdByExamID(String examId,String userId);

    List<Paper> getPaperListByExamId(String examId);
}
