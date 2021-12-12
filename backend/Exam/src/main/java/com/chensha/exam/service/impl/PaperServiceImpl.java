package com.chensha.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.chensha.exam.dao.mapper.PaperMapper;
import com.chensha.exam.dao.mapper.QuestionCollectMapper;
import com.chensha.exam.dao.mapper.QuestionMapper;
import com.chensha.exam.dao.pojo.Paper;
import com.chensha.exam.dao.pojo.Question;
import com.chensha.exam.dao.pojo.QuestionCollect;
import com.chensha.exam.service.PaperService;
import com.chensha.exam.service.SysPaperService;
import com.chensha.exam.service.SysUserService;
import com.chensha.exam.utils.JWTUtils;
import com.chensha.exam.vo.ErrorCode;
import com.chensha.exam.vo.ExamVo;
import com.chensha.exam.vo.Result;
import com.chensha.exam.vo.params.PaperParams;
import com.chensha.exam.vo.params.UpdateQuesParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    @Autowired
    public QuestionMapper questionMapper;
    @Autowired
    public QuestionCollectMapper questionCollectMapper;


    @Override
    public Result listMyExam(String authHeader) {
        String account = JWTUtils.getAccount(authHeader.substring(7));
        if(account==null){
            return Result.fail(ErrorCode.NO_PERMISSION.getCode(), ErrorCode.NO_PERMISSION.getMsg());
        }
        String userid = sysUserService.getIdByAccount(account);
        LambdaQueryWrapper<Paper> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Paper::getPaperUser,userid);
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
        String userId = sysUserService.getIdByAccount(owner);
        Paper paper = sysPaperService.getPaperByExamId(examId,userId);

        //校验是否已经开始

        paper.setPaperTimeJoin(System.currentTimeMillis());
        paperMapper.updateById(paper);

        return Result.success(sysPaperService.getQuesLiteVoByExamId(examId));
    }

    @Override
    public Result addPaper(PaperParams paperParams, String authHeader) {
        String owner = JWTUtils.getAccount(authHeader.substring(7));
        if ( paperParams == null || StringUtils.isEmpty(paperParams.getPaperLink()) ||
                StringUtils.isEmpty(paperParams.getPaperUser())) {
            return Result.fail(ErrorCode.ERROR_PARAMETER.getCode(),ErrorCode.ERROR_PARAMETER.getMsg());
        }

        Paper paper = new Paper();
        paper.setPaperUser(paperParams.getPaperUser());
        paper.setPaperLink(paperParams.getPaperLink());
        paperMapper.insert(paper);

        return Result.success("成功");
    }

    @Override
    public Result getPaper(String examId, String authHeader) {
        if (StringUtils.isBlank(examId)) {
            return Result.fail(ErrorCode.ERROR_PARAMETER.getCode(),ErrorCode.ERROR_PARAMETER.getMsg());
        }


        String owner = JWTUtils.getAccount(authHeader.substring(7));
        String userId = sysUserService.getIdByAccount(owner);

        return Result.success(sysPaperService.getQuesLiteVoByExamId(examId));
    }

    @Override
    public Result updateOne(UpdateQuesParams updateQuesParams, String authHeader) {
        //判空

        String paperId=sysPaperService.getPaperIdByExamID(updateQuesParams.getExamId());

        //去重,判断超时

        QuestionCollect questionCollect = new QuestionCollect();
        questionCollect.setCollectLinkQuestion(updateQuesParams.getQuestionId());
        questionCollect.setCollectLinkPaper(paperId);
        questionCollect.setCollectText(updateQuesParams.getQuestionAnswer());

        questionCollectMapper.insert(questionCollect);
        return Result.success("成功");
    }

    @Override
    public Result autoCorrect(String examId, String authHeader) {
        //认证

        List<Paper> paperList=sysPaperService.getPaperListByExamId(examId);
        for (Paper paper : paperList) {
            LambdaQueryWrapper<QuestionCollect> lambdaQueryWrap= new LambdaQueryWrapper<>();
            lambdaQueryWrap.eq(QuestionCollect::getCollectLinkPaper,paper.getPaperId());
            List<QuestionCollect> quesList=questionCollectMapper.selectList(lambdaQueryWrap);

            //以后升级redis
            for(QuestionCollect ques : quesList){
                LambdaQueryWrapper<Question> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(Question::getQuestionId,ques.getCollectLinkQuestion());
                queryWrapper.last("limit 1");

                Question answer=questionMapper.selectOne(queryWrapper);
                int quesType = answer.getQuestionType();
                int fullMark = answer.getQuestionScore();
                if (quesType == 0 || quesType == 1 ||quesType == 2) {
                    if(judgeQues(ques.getCollectText(), answer.getQuestionRightChoice())){
                        //正确
                        setScore(ques.getCollectId(),fullMark);
                    }else {
                        //错误
                        setScore(ques.getCollectId(),0);
                    }
                }

            }
        }

        return null;
    }


    public boolean judgeQues(String text,String answer){
        String[] textSplit  = text.split("-");
        String[] answerSplit = answer.split("-");

        if(answerSplit != null && textSplit != null && textSplit.length == answerSplit.length){

            for (int i = 0; i < answerSplit.length-1; i++) {
                boolean flag = false;
                for (int j = 0; j < textSplit.length-1; j++){
                    if (textSplit[j] == answer) {
                        flag = true;
                        break;
                    }
                }

                if (flag == false) {
                    return false;
                }
            }

        }
    return false;
    }

    public void setScore(String quesCollectId,int score){
        QuestionCollect questionCollect = new QuestionCollect();
        questionCollect.setCollectId(quesCollectId);;
        questionCollect.setCollectScore(score);

        questionCollectMapper.updateById(questionCollect);
    }
}
