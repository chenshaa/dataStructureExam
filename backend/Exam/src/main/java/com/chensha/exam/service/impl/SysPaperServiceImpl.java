package com.chensha.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chensha.exam.dao.mapper.ExamMapper;
import com.chensha.exam.dao.mapper.PaperMapper;
import com.chensha.exam.dao.pojo.Exam;
import com.chensha.exam.dao.pojo.Paper;
import com.chensha.exam.service.SysPaperService;
import com.chensha.exam.vo.ExamVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysPaperServiceImpl implements SysPaperService {
    @Autowired
    public ExamMapper examMapper;
    @Autowired
    public PaperMapper paperMapper;

    public ExamVo getExamVoById(String id){
        LambdaQueryWrapper<Exam> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Exam::getExamId,id);
        queryWrapper.last("limit 1");
        Exam exam = examMapper.selectOne(queryWrapper);

        ExamVo examVo = new ExamVo();
        BeanUtils.copyProperties(exam, examVo);

        return examVo;
    }

    public Exam getExamById(String id){
        LambdaQueryWrapper<Exam> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Exam::getExamId,id);
        queryWrapper.last("limit 1");

        return examMapper.selectOne(queryWrapper);
    }

    public Paper getPaperByExamId(String examId, String owner){
        LambdaQueryWrapper<Paper> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Paper::getPaperLink,examId);
        queryWrapper.last("limit 1");

        return paperMapper.selectOne(queryWrapper);
    }
}
