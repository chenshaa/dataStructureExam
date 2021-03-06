package com.chensha.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chensha.exam.dao.mapper.ExamMapper;
import com.chensha.exam.dao.mapper.PaperMapper;
import com.chensha.exam.dao.mapper.QuestionMapper;
import com.chensha.exam.dao.pojo.Exam;
import com.chensha.exam.dao.pojo.Paper;
import com.chensha.exam.dao.pojo.Question;
import com.chensha.exam.service.SysPaperService;
import com.chensha.exam.vo.ExamVo;
import com.chensha.exam.vo.QuestionLiteVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysPaperServiceImpl implements SysPaperService {
    public final ExamMapper examMapper;
    public final PaperMapper paperMapper;
    public final QuestionMapper questionMapper;

    public SysPaperServiceImpl(ExamMapper examMapper, PaperMapper paperMapper, QuestionMapper questionMapper) {
        this.examMapper = examMapper;
        this.paperMapper = paperMapper;
        this.questionMapper = questionMapper;
    }

    public ExamVo getExamVoById(String id) {
        LambdaQueryWrapper<Exam> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Exam::getExamId, id);
        queryWrapper.last("limit 1");
        Exam exam = examMapper.selectOne(queryWrapper);

        ExamVo examVo = new ExamVo();
        BeanUtils.copyProperties(exam, examVo);

        return examVo;
    }

    public Exam getExamById(String id) {
        LambdaQueryWrapper<Exam> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Exam::getExamId, id);
        queryWrapper.last("limit 1");

        return examMapper.selectOne(queryWrapper);
    }

    public Paper getPaperByExamId(String examId, String userId) {
        LambdaQueryWrapper<Paper> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Paper::getPaperLink, examId);
        queryWrapper.eq(Paper::getPaperUser, userId);
        queryWrapper.last("limit 1");

        return paperMapper.selectOne(queryWrapper);
    }

    public List<Paper> getPaperListByExamId(String examId) {
        LambdaQueryWrapper<Paper> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Paper::getPaperLink, examId);

        return paperMapper.selectList(queryWrapper);
    }

    public List<QuestionLiteVo> getQuesLiteVoByExamId(String examId) {
        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Question::getQuestionLink, examId);
        List<Question> questionList = questionMapper.selectList(queryWrapper);

        List<QuestionLiteVo> questionLiteVos = new ArrayList<>();
        for (Question question : questionList) {
            QuestionLiteVo questionLiteVo = new QuestionLiteVo();
            BeanUtils.copyProperties(question, questionLiteVo);
            questionLiteVos.add(questionLiteVo);
        }
        return questionLiteVos;
    }

    @Override
    public String getPaperIdByExamID(String examId, String userId) {
        LambdaQueryWrapper<Paper> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Paper::getPaperLink, examId);
        lambdaQueryWrapper.eq(Paper::getPaperUser, userId);
        Paper paper = paperMapper.selectOne(lambdaQueryWrapper);
        return paper.getPaperId();
    }
}
