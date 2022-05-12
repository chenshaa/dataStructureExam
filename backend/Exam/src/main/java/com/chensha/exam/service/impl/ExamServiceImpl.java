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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExamServiceImpl implements ExamService {

    private final SysUserService sysUserService;
    private final ExamMapper examMapper;

    public ExamServiceImpl(SysUserService sysUserService, ExamMapper examMapper) {
        this.sysUserService = sysUserService;
        this.examMapper = examMapper;
    }

    /**
     * 由管理员或者教师列出所有考试
     *
     * @param authHeader token认证头
     * @return List<ExamVo> 全部考试列表
     */
    @Override
    public Result listExam(String authHeader) {
        if (sysUserService.authHeader4Admin(authHeader) == null) {
            return Result.fail(ErrorCode.NO_PERMISSION.getCode(), ErrorCode.NO_PERMISSION.getMsg());
        }

        LambdaQueryWrapper<Exam> queryWrapper = new LambdaQueryWrapper<>();
        List<Exam> examList = examMapper.selectList(queryWrapper);
        return Result.success(copyList(examList));
    }

    /**
     * 由管理员或者教师增加一场考试
     *
     * @param authHeader token认证头
     * @param examParams 考试详情params
     * @return 成功
     */
    @Override
    public Result addExam(String authHeader, ExamParams examParams) {
        if (sysUserService.authHeader4Admin(authHeader) == null) {
            return Result.fail(ErrorCode.NO_PERMISSION.getCode(), ErrorCode.NO_PERMISSION.getMsg());
        }

        if (examParams == null || StringUtils.isBlank(examParams.getExamName()) || StringUtils.isBlank(examParams.getExamDescription())) {
            return Result.fail(ErrorCode.ERROR_PARAMETER.getCode(), ErrorCode.ERROR_PARAMETER.getMsg());
        }

        if (getExamByName(examParams.getExamName()) != null) {
            return Result.fail(ErrorCode.OBJECT_EXISTS.getCode(), ErrorCode.OBJECT_EXISTS.getMsg());
        }

        Exam exam = new Exam();
        BeanUtils.copyProperties(examParams, exam);
        exam.setExamCreateTime(System.currentTimeMillis());
        exam.setExamUpdateTime(System.currentTimeMillis());
        examMapper.insert(exam);
        return Result.success("成功");
    }

    /**
     * 由管理员或者教师结束一场考试
     *
     * @param examId     考试id
     * @param timestamp  结束时间戳，留空则为现在
     * @param authHeader token认证头
     * @return 成功
     */
    @Override
    public Result setExamEnd(String examId, Long timestamp, String authHeader) {
        if (sysUserService.authHeader4Admin(authHeader) == null) {
            return Result.fail(ErrorCode.NO_PERMISSION.getCode(), ErrorCode.NO_PERMISSION.getMsg());
        }

        if (StringUtils.isBlank(examId)) {
            return Result.fail(ErrorCode.ERROR_PARAMETER.getCode(), ErrorCode.ERROR_PARAMETER.getMsg());
        }

        Exam exam = getExamById(examId);
        if (exam == null) {
            return Result.fail(ErrorCode.OBJECT_MISSED.getCode(), ErrorCode.OBJECT_MISSED.getMsg());
        }
        Long time = timestamp;
        if (timestamp == null) {
            time = System.currentTimeMillis();
        }

        exam.setExamEndTime(time);
        examMapper.updateById(exam);
        return Result.success("成功");
    }

    /**
     * 由管理员或者教师开始一场考试
     *
     * @param examId     考试id
     * @param timestamp  开始时间戳，留空则为现在
     * @param authHeader token认证头
     * @return 成功
     */
    @Override
    public Result setExamStart(String examId, Long timestamp, String authHeader) {
        if (sysUserService.authHeader4Admin(authHeader) == null) {
            return Result.fail(ErrorCode.NO_PERMISSION.getCode(), ErrorCode.NO_PERMISSION.getMsg());
        }

        if (StringUtils.isBlank(examId)) {
            return Result.fail(ErrorCode.ERROR_PARAMETER.getCode(), ErrorCode.ERROR_PARAMETER.getMsg());
        }

        Exam exam = getExamById(examId);
        if (exam == null) {
            return Result.fail(ErrorCode.OBJECT_MISSED.getCode(), ErrorCode.OBJECT_MISSED.getMsg());
        }
        Long time = timestamp;
        if (timestamp == null) {
            time = System.currentTimeMillis();
        }

        exam.setExamStartTime(time);
        examMapper.updateById(exam);
        return Result.success("成功");
    }

    public List<ExamVo> copyList(List<Exam> examList) {
        List<ExamVo> examVoList = new ArrayList<>();
        for (Exam exam : examList) {
            ExamVo examVo = new ExamVo();
            BeanUtils.copyProperties(exam, examVo);
            examVoList.add(examVo);
        }
        return examVoList;
    }

    public Exam getExamByName(String name) {
        LambdaQueryWrapper<Exam> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Exam::getExamName, name);
        queryWrapper.last("limit 1");

        return (examMapper.selectOne(queryWrapper));
    }

    public Exam getExamById(String examId) {
        LambdaQueryWrapper<Exam> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Exam::getExamId, examId);
        queryWrapper.last("limit 1");

        return (examMapper.selectOne(queryWrapper));
    }
}
