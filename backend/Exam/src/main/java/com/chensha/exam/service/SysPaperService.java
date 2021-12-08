package com.chensha.exam.service;

import com.chensha.exam.dao.pojo.Exam;
import com.chensha.exam.dao.pojo.Paper;
import com.chensha.exam.vo.ExamVo;

public interface SysPaperService {
    public ExamVo getExamVoById(String id);

    public Exam getExamById(String id);

    public Paper getPaperByExamId(String examId, String owner);
}
