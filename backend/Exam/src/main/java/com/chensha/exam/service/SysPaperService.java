package com.chensha.exam.service;

import com.chensha.exam.dao.pojo.Exam;
import com.chensha.exam.dao.pojo.Paper;
import com.chensha.exam.vo.ExamVo;
import com.chensha.exam.vo.QuestionLiteVo;

import java.util.List;

public interface SysPaperService {
    public ExamVo getExamVoById(String id);

    public Exam getExamById(String id);

    public Paper getPaperByExamId(String examId, String owner);

    public List<QuestionLiteVo> getQuesLiteVoByExamId(String examId);

    String getPaperIdByExamID(String examId);
}
