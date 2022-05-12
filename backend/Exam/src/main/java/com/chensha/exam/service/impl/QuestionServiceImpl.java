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
import com.chensha.exam.vo.params.LinkQuesParams;
import com.chensha.exam.vo.params.QuestionParams;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    public final SysUserService sysUserService;
    public final QuestionMapper questionMapper;

    public QuestionServiceImpl(SysUserService sysUserService, QuestionMapper questionMapper) {
        this.sysUserService = sysUserService;
        this.questionMapper = questionMapper;
    }

    /**
     * 管理员或教师列出所有题目
     *
     * @param authHeader 认证头
     * @return List<QuestionVo>
     */
    @Override
    public Result listAllQues(String authHeader) {
        if (sysUserService.authHeader4Admin(authHeader) == null) {
            return Result.fail(ErrorCode.NO_PERMISSION.getCode(), ErrorCode.NO_PERMISSION.getMsg());
        }

        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        List<Question> quesList = questionMapper.selectList(queryWrapper);
        return Result.success(copyList(quesList));
    }

    /**
     * 由管理员或教师添加一道题目
     *
     * @param authHeader     认证头
     * @param questionParams 题目参数
     * @return 成功
     */
    @Override
    public Result addQues(String authHeader, QuestionParams questionParams) {
        if (sysUserService.authHeader4Admin(authHeader) == null) {
            return Result.fail(ErrorCode.NO_PERMISSION.getCode(), ErrorCode.NO_PERMISSION.getMsg());
        }

        if (questionParams == null || StringUtils.isBlank(questionParams.getQuestionText())
                || questionParams.getQuestionScore() == null || questionParams.getQuestionType() == null) {
            return Result.fail(ErrorCode.ERROR_PARAMETER.getCode(), ErrorCode.ERROR_PARAMETER.getMsg());
        }

        /*题目就不判重了
        if(getQuestionByName(questionParams.getQuestionText()) != null ){
            return Result.fail(ErrorCode.OBJECT_EXISTS.getCode(),ErrorCode.OBJECT_EXISTS.getMsg());
        }*/

        //对需要答案的题型进一步校验
        int questionType = questionParams.getQuestionType();
        if (questionType == 0 || questionType == 1 || questionType == 2 || questionType == 4) {
            if (StringUtils.isBlank(questionParams.getQuestionOpinion1()) || StringUtils.isBlank(questionParams.getQuestionRightChoice())) {
                return Result.fail(ErrorCode.ERROR_PARAMETER.getCode(), ErrorCode.ERROR_PARAMETER.getMsg());
            }
        }

        Question question = new Question();
        BeanUtils.copyProperties(questionParams, question);
        question.setQuestionCreateTime(System.currentTimeMillis());
        question.setQuestionUpdateTime(System.currentTimeMillis());

        questionMapper.insert(question);
        return Result.success("成功");
    }

    public List<QuestionVo> copyList(List<Question> quesList) {
        List<QuestionVo> quesVoList = new ArrayList<>();
        for (Question ques : quesList) {
            QuestionVo quesVo = new QuestionVo();
            BeanUtils.copyProperties(ques, quesVo);
            quesVoList.add(quesVo);
        }
        return quesVoList;
    }

    public Question getQuestionByName(String name) {
        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Question::getQuestionText, name);
        queryWrapper.last("limit 1");

        return (questionMapper.selectOne(queryWrapper));
    }

    public int getQuesTypeById(String id) {
        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Question::getQuestionId, id);
        queryWrapper.last("limit 1");

        return (questionMapper.selectOne(queryWrapper).getQuestionType());
    }

    /**
     * 管理员或教师根据考试ID列出已经添加的题目
     *
     * @param examid     考试id
     * @param authHeader 认证头
     * @return List<QuestionVo>
     */
    @Override
    public Result listQues(String examid, String authHeader) {
        if (StringUtils.isBlank(examid)) {
            return Result.fail(ErrorCode.ERROR_PARAMETER.getCode(), ErrorCode.ERROR_PARAMETER.getMsg());
        }

        if (sysUserService.authHeader4Admin(authHeader) == null) {
            return Result.fail(ErrorCode.NO_PERMISSION.getCode(), ErrorCode.NO_PERMISSION.getMsg());
        }

        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Question::getQuestionLink, examid);
        List<Question> quesList = questionMapper.selectList(queryWrapper);
        return Result.success(copyList(quesList));
    }

    /**
     * 根据题目ID得到题目
     *
     * @param quesid     题目id
     * @param authHeader 认证头
     * @return 题目
     */
    @Override
    public Result getQuesById(String quesid, String authHeader) {
        if (sysUserService.authHeader4Admin(authHeader) == null) {
            return Result.fail(ErrorCode.NO_PERMISSION.getCode(), ErrorCode.NO_PERMISSION.getMsg());
        }

        if (StringUtils.isBlank(quesid)) {
            return Result.fail(ErrorCode.ERROR_PARAMETER.getCode(), ErrorCode.ERROR_PARAMETER.getMsg());
        }

        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Question::getQuestionId, quesid);

        return Result.success(questionMapper.selectOne(queryWrapper));
    }

    /**
     * 将试题关联至一张试卷
     *
     * @param authHeader     认证头
     * @param questionParams questionId和questionLink
     * @return 成功
     */
    @Override
    public Result linkQues(String authHeader, LinkQuesParams questionParams) {
        //params判空
        if (StringUtils.isBlank(questionParams.getQuestionId()) || StringUtils.isBlank(questionParams.getQuestionLink())) {
            return Result.fail(ErrorCode.ERROR_PARAMETER.getCode(), ErrorCode.ERROR_PARAMETER.getMsg());
        }

        if (sysUserService.authHeader4Admin(authHeader) == null) {
            return Result.fail(ErrorCode.NO_PERMISSION.getCode(), ErrorCode.NO_PERMISSION.getMsg());
        }

        Question question = new Question();
        question.setQuestionId(questionParams.getQuestionId());
        question.setQuestionLink(questionParams.getQuestionLink());
        questionMapper.updateById(question);
        return Result.success("成功");
    }

    /**
     * 将试题从关联的试卷中移除
     *
     * @param authHeader     认证头
     * @param questionParams questionId
     * @return 成功
     */
    public Result disLinkQues(String authHeader, LinkQuesParams questionParams) {
        //校验
        if (StringUtils.isBlank(questionParams.getQuestionId())) {
            return Result.fail(ErrorCode.ERROR_PARAMETER.getCode(), ErrorCode.ERROR_PARAMETER.getMsg());
        }

        if (sysUserService.authHeader4Admin(authHeader) == null) {
            return Result.fail(ErrorCode.NO_PERMISSION.getCode(), ErrorCode.NO_PERMISSION.getMsg());
        }

        Question question = new Question();
        question.setQuestionId(questionParams.getQuestionId());
        question.setQuestionLink("");
        questionMapper.updateById(question);

        return Result.success("成功");
    }

    /**
     * 统计一场考试有多少道试题
     *
     * @param examid 考试id
     * @return 数量
     */
    @Override
    public Integer countQues(String examid) {
        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Question::getQuestionLink, examid);
        List<Question> quesList = questionMapper.selectList(queryWrapper);

        return quesList.size();
    }

    /**
     * 计算一场考试的满分
     *
     * @param examid 考试id
     * @return 分数
     */
    @Override
    public Float getFullScore(String examid) {
        LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Question::getQuestionLink, examid);
        List<Question> quesList = questionMapper.selectList(queryWrapper);

        Float count = (float) 0;
        for (Question question : quesList) {
            count += question.getQuestionScore();
        }

        return count;
    }
}
