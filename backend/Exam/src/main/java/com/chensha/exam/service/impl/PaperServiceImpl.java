package com.chensha.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.chensha.exam.dao.mapper.PaperMapper;
import com.chensha.exam.dao.pojo.Paper;
import com.chensha.exam.service.PaperService;
import com.chensha.exam.service.SysPaperService;
import com.chensha.exam.service.SysUserService;
import com.chensha.exam.utils.JWTUtils;
import com.chensha.exam.vo.ErrorCode;
import com.chensha.exam.vo.ExamVo;
import com.chensha.exam.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.chensha.exam.vo.params.PaperParams;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaperServiceImpl implements PaperService {

    @Autowired
    public SysUserService sysUserService;
    @Autowired
    public PaperMapper paperMapper;
    @Autowired
    public SysPaperService sysPaperService;


    @Override
    public Result listMyExam(String authHeader) {
        String owner = sysUserService.authToken(authHeader);
        if(owner==null){
            return Result.fail(ErrorCode.NO_PERMISSION.getCode(), ErrorCode.NO_PERMISSION.getMsg());
        }

        LambdaQueryWrapper<Paper> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Paper::getPaperUser,owner);
        List<Paper> paperList = paperMapper.selectList(queryWrapper);

        List<ExamVo> examVoList = new ArrayList<>();
        for (Paper paper : paperList) {
            ExamVo examVo = sysPaperService.getExamVoById(paper.getPaperLink());
            examVoList.add(examVo);
        }

        return Result.success(examVoList);
    }

    @Override
    public Result startExam(String examId, String authHeader) {
        if (StringUtils.isBlank(examId)) {
            return Result.fail(ErrorCode.ERROR_PARAMETER.getCode(),ErrorCode.ERROR_PARAMETER.getMsg());
        }

        String owner = JWTUtils.getAccount(authHeader.substring(7));
        Paper paper = sysPaperService.getPaperByExamId(examId,owner);

        paper.setPaperTimeJoin(System.currentTimeMillis());
        paperMapper.updateById(paper);
        return Result.success("成功");
    }

    @Override
    public Result addPaper(PaperParams paperParams, String authHeader) {
        String owner = JWTUtils.getAccount(authHeader.substring(7));
        if (StringUtils.isEmpty(owner) || paperParams != null ||
                StringUtils.isEmpty(paperParams.getPaperLink()) || StringUtils.isEmpty(paperParams.getPaperUser())) {
            return Result.fail(ErrorCode.ERROR_PARAMETER.getCode(),ErrorCode.ERROR_PARAMETER.getMsg());
        }

        Paper paper = new Paper();
        paper.setPaperUser(paperParams.getPaperUser());
        paper.setPaperLink(paperParams.getPaperLink());
        paperMapper.insert(paper);

        return Result.success("成功");
    }


}
