package com.chensha.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.chensha.exam.dao.mapper.ExamMapper;
import com.chensha.exam.dao.pojo.Exam;
import com.chensha.exam.service.ExamService;
import com.chensha.exam.service.SysUserService;
import com.chensha.exam.vo.ErrorCode;
import com.chensha.exam.vo.ExamVo;
import com.chensha.exam.vo.Result;
import com.chensha.exam.vo.params.ExamParams;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExamServiceImpl implements ExamService {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ExamMapper examMapper;

    @Override
    public Result listExam(String authHeader) {
        if(sysUserService.authToken(authHeader)==null){
            return Result.fail(ErrorCode.NO_PERMISSION.getCode(), ErrorCode.NO_PERMISSION.getMsg());
        }

        LambdaQueryWrapper<Exam> queryWrapper = new LambdaQueryWrapper<>();
        List<Exam> examList = examMapper.selectList(queryWrapper);
        return Result.success(copyList(examList));
    }

    @Override
    public Result addExam(String authHeader, ExamParams examParams) {
        if(sysUserService.authToken(authHeader)==null){
            return Result.fail(ErrorCode.NO_PERMISSION.getCode(), ErrorCode.NO_PERMISSION.getMsg());
        }

        if(examParams == null || StringUtils.isBlank(examParams.getExamName()) || StringUtils.isBlank(examParams.getExamDescription())
                || examParams.getExamEndTime() == 0 || examParams.getExamStartTime() == 0){
            return Result.fail(ErrorCode.ERROR_PARAMETER.getCode(),ErrorCode.ERROR_PARAMETER.getMsg());
        }

        if(getExamByName(examParams.getExamName()) != null ){
            return Result.fail(ErrorCode.OBJECT_EXISTS.getCode(),ErrorCode.OBJECT_EXISTS.getMsg());
        }

        Exam exam = new Exam();
        BeanUtils.copyProperties(examParams, exam);
        exam.setExamCreateTime(System.currentTimeMillis());
        exam.setExamUpdateTime(System.currentTimeMillis());
        examMapper.insert(exam);
        return Result.success("成功");
    }

    public List<ExamVo> copyList(List<Exam> examList){
        List<ExamVo> examVoList = new ArrayList<>();
        for (Exam exam : examList) {
            ExamVo examVo = new ExamVo();
            BeanUtils.copyProperties(exam, examVo);
            examVoList.add(examVo);
        }
        return examVoList;
    }

    public Exam getExamByName(String name){
        LambdaQueryWrapper<Exam> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Exam::getExamName,name);
        queryWrapper.last("limit 1");

        return (examMapper.selectOne(queryWrapper));
    }
}
