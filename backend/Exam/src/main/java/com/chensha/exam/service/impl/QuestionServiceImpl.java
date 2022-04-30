package com.chensha.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.chensha.exam.dao.mapper.QuestionMapper;
import com.chensha.exam.dao.pojo.Question;
import com.chensha.exam.service.QuestionService;
import com.chensha.exam.service.SysUserService;
import com.chensha.exam.vo.ErrorCode;
import com.chensha.exam.vo.QuestionVo;
import com.chensha.exam.vo.Result;
import com.chensha.exam.vo.params.QuestionParams;
import com.chensha.exam.vo.params.Uni2Params;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    public SysUserService sysUserService;
    @Autowired
    public QuestionMapper questionMapper;

    @Override
    public Result listAllQues(String authHeader) {
        if(sysUserService.authTokenAdmin(authHeader)==null){
            return Result.fail(ErrorCode.NO_PERMISSION.getCode(), ErrorCode.NO_PERMISSION.getMsg());
        }

        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        List<Question> quesList = questionMapper.selectList(queryWrapper);
        return Result.success(copyList(quesList));
    }

    @Override
    public Result addQues(String authHeader, QuestionParams questionParams) {
        if(sysUserService.authTokenAdmin(authHeader)==null){
            return Result.fail(ErrorCode.NO_PERMISSION.getCode(), ErrorCode.NO_PERMISSION.getMsg());
        }

        if(questionParams == null || StringUtils.isBlank(questionParams.getQuestionText())
                || questionParams.getQuestionScore() == null || questionParams.getQuestionType() == null){
            return Result.fail(ErrorCode.ERROR_PARAMETER.getCode(),ErrorCode.ERROR_PARAMETER.getMsg());
        }

        /*题目就不判重了
        if(getQuestionByName(questionParams.getQuestionText()) != null ){
            return Result.fail(ErrorCode.OBJECT_EXISTS.getCode(),ErrorCode.OBJECT_EXISTS.getMsg());
        }*/

        //对需要答案的题型进一步校验
        int questionType = questionParams.getQuestionType();
        if (questionType == 0 || questionType == 1 ||questionType == 2 || questionType == 4 ) {
            if(StringUtils.isBlank(questionParams.getQuestionOpinion1()) || StringUtils.isBlank(questionParams.getQuestionRightChoice())){
                return Result.fail(ErrorCode.ERROR_PARAMETER.getCode(),ErrorCode.ERROR_PARAMETER.getMsg());
            }
        }

        Question question = new Question();
        BeanUtils.copyProperties(questionParams, question);
        question.setQuestionCreateTime(System.currentTimeMillis());
        question.setQuestionUpdateTime(System.currentTimeMillis());

        questionMapper.insert(question);
        return Result.success("成功");
    }


    public List<QuestionVo> copyList(List<Question> quesList){
        List<QuestionVo> quesVoList = new ArrayList<>();
        for (Question ques : quesList) {
            QuestionVo quesVo = new QuestionVo();
            BeanUtils.copyProperties(ques, quesVo);
            quesVoList.add(quesVo);
        }
        return quesVoList;
    }

    public Question getQuestionByName(String name){
        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Question::getQuestionText,name);
        queryWrapper.last("limit 1");

        return (questionMapper.selectOne(queryWrapper));
    }

    public int getQuesTypeById(String id){
        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Question::getQuestionId,id);
        queryWrapper.last("limit 1");

        return (questionMapper.selectOne(queryWrapper).getQuestionType());
    }

    @Override
    public Result listQues(String examid, String authHeader) {
        if(sysUserService.authTokenAdmin(authHeader)==null){
            return Result.fail(ErrorCode.NO_PERMISSION.getCode(), ErrorCode.NO_PERMISSION.getMsg());
        }

        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Question ::getQuestionLink,examid);
        List<Question> quesList = questionMapper.selectList(queryWrapper);
        return Result.success(copyList(quesList));
    }

    @Override
    public Result getQuesById(String quesid, String authHeader) {
        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Question::getQuestionId,quesid);

        return Result.success(questionMapper.selectOne(queryWrapper));
    }

    @Override
    public Result linkQues(String authHeader, Uni2Params questionParams) {
        //params判空--todo

        Question question = new Question();
        question.setQuestionId(questionParams.getParam1());
        question.setQuestionLink(questionParams.getParam2());
        questionMapper.updateById(question);
        return Result.success("成功");
    }

    @Override
    public Result removeQues(String authHeader, Uni2Params questionParams) {
        // 校验--todo

        Question question = new Question();
        question.setQuestionId(questionParams.getParam1());
        question.setQuestionLink("");
        questionMapper.updateById(question);

        return Result.success("成功");
    }

    @Override
    public Integer countQues(String examid) {
        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Question ::getQuestionLink,examid);
        List<Question> quesList = questionMapper.selectList(queryWrapper);

        return quesList.size();
    }

    @Override
    public Float getFullScore(String examid) {
        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Question ::getQuestionLink,examid);
        List<Question> quesList = questionMapper.selectList(queryWrapper);

        Float count = Float.valueOf(0);
        for (Question question : quesList) {
            count += question.getQuestionScore();
        }

        return count;
    }
}
