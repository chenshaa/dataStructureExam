package com.chensha.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.chensha.exam.dao.mapper.PaperMapper;
import com.chensha.exam.dao.mapper.QuestionCollectMapper;
import com.chensha.exam.dao.mapper.QuestionMapper;
import com.chensha.exam.dao.pojo.Paper;
import com.chensha.exam.dao.pojo.Question;
import com.chensha.exam.dao.pojo.QuestionCollect;
import com.chensha.exam.dao.pojo.User;
import com.chensha.exam.service.PaperService;
import com.chensha.exam.service.QuestionService;
import com.chensha.exam.service.SysPaperService;
import com.chensha.exam.service.SysUserService;
import com.chensha.exam.utils.JWTUtils;
import com.chensha.exam.vo.*;
import com.chensha.exam.vo.params.PaperParams;
import com.chensha.exam.vo.params.UpdateQuesParams;
import com.chensha.exam.vo.params.UpdateScore;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
    @Autowired
    public QuestionService questionService;

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
        String userId = sysUserService.getIdByAccount(JWTUtils.getAccount(authHeader.substring(7)));
        String paperId=sysPaperService.getPaperIdByExamID(updateQuesParams.getExamId(),userId);

        //去重,判断超时

        QuestionCollect questionCollect = new QuestionCollect();
        questionCollect.setCollectLinkQuestion(updateQuesParams.getQuestionId());
        questionCollect.setCollectLinkPaper(paperId);
        questionCollect.setCollectText(updateQuesParams.getQuestionAnswer());

        questionCollectMapper.insert(questionCollect);
        return Result.success("成功");
    }

    /**
     * 自动批改试卷
     * @param examId 考试id
     * @param authHeader 认证头
     * @return null
     */
    @Override
    public Result autoCorrect(String examId, String authHeader) {
        //认证

        //todo：升级redis
        //question: [questionType, questionFullMark, RightChoice]
        Map<String,List<String>> localAnswerMap = new HashMap<>();

        //得到总共的试题id列表
        LambdaQueryWrapper<Question> quesQueryWrapper = new LambdaQueryWrapper<>();
        quesQueryWrapper.eq(Question ::getQuestionLink,examId);
        List<Question> tempList = questionMapper.selectList(quesQueryWrapper);
        //id列表
        List<String> totalQuesList = new ArrayList<>();
        for (Question question : tempList) {
            totalQuesList.add(question.getQuestionId());

            //填充试题集
            List<String> temp = new ArrayList<>();
            temp.add(String.valueOf(question.getQuestionType()));
            temp.add(String.valueOf(question.getQuestionScore()));
            temp.add(question.getQuestionRightChoice());
            localAnswerMap.put(question.getQuestionId(), temp);
        }

        List<Paper> paperList=sysPaperService.getPaperListByExamId(examId);
        for (Paper paper : paperList) {
            //由试卷枚举个人已提交的记录
            LambdaQueryWrapper<QuestionCollect> lambdaQueryWrap= new LambdaQueryWrapper<>();
            lambdaQueryWrap.eq(QuestionCollect::getCollectLinkPaper,paper.getPaperId());
            List<QuestionCollect> quesList=questionCollectMapper.selectList(lambdaQueryWrap);

            //个人的题目列表
            List<String> personalQuesList = new ArrayList<>();
            for (QuestionCollect questionCollect : quesList) {
                personalQuesList.add(questionCollect.getCollectLinkQuestion());
            }

            for(String quesId : totalQuesList){
                //初始化
                List<String> answerList = null;
                //本地数据集查询
                for(Map.Entry<String,List<String>> entry : localAnswerMap.entrySet()){
                    if (entry.getKey().equals(quesId)) {
                        answerList=entry.getValue();
                    }
                }
                //题目类型
                String quesType = answerList.get(0);
                //是否自动批改
                boolean autoCorrectSwitch = Objects.equals(quesType, "0") || Objects.equals(quesType, "1") || Objects.equals(quesType, "2");

                if(personalQuesList.contains(quesId)){
                    //已答题

                    QuestionCollect ques = new QuestionCollect();
                    for (QuestionCollect queryQues :quesList) {
                        if (Objects.equals(queryQues.getCollectLinkQuestion(),quesId)) {
                            BeanUtils.copyProperties(queryQues,ques);
                            break;
                        }
                    }

                    if (autoCorrectSwitch) {
                        if(judgeQues(ques.getCollectText(), answerList.get(2))){
                            //正确
                            int fullMark = Integer.parseInt(answerList.get(1));
                            setScore(ques.getCollectId(), Float.valueOf(fullMark));
                        }else {
                            //错误
                            setScore(ques.getCollectId(), Float.valueOf(0));
                        }
                    }


                }else {
                    //未答题
                    if (autoCorrectSwitch) {
                        QuestionCollect questionCollect = new QuestionCollect();

                        questionCollect.setCollectScore(Float.valueOf(0));
                        questionCollect.setCollectLinkQuestion(quesId);
                        questionCollect.setCollectLinkPaper(paper.getPaperId());
                        questionCollect.setCollectText("");

                        questionCollectMapper.insert(questionCollect);
                    }

                }
            }

        }

        return Result.success("成功");
    }

    @Override
    public Result listStu(String examId, String authHeader) {
        if (StringUtils.isBlank(examId)) {
            return Result.fail(ErrorCode.ERROR_PARAMETER.getCode(),ErrorCode.ERROR_PARAMETER.getMsg());
        }

        LambdaQueryWrapper<Paper> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Paper::getPaperLink,examId);
        List<Paper> paperList = paperMapper.selectList(queryWrapper);

        List<User> userList=new ArrayList<User>();
        for (Paper paper : paperList) {
            userList.add(sysUserService.getUserById(paper.getPaperUser()));
        }

        return Result.success(sysUserService.copyList(userList));
    }

    /**
     * 返回考试情况
     * @param examId 考试ID
     * @param authHeader 认证头
     * @return
     */
    @Override
    public Result correctProgress(String examId, String authHeader) {
        List<correctProgressVo> correctProgressVos = new ArrayList<>();
        Integer questionNum = questionService.countQues(examId);
        Float fullScore = questionService.getFullScore(examId);

        LambdaQueryWrapper<Paper> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Paper::getPaperLink,examId);
        List<Paper> paperList = paperMapper.selectList(queryWrapper);

        for (Paper paper : paperList) {
            LambdaQueryWrapper<QuestionCollect> quesQueryWrapper = new LambdaQueryWrapper<>();
            quesQueryWrapper.eq(QuestionCollect::getCollectLinkPaper,paper.getPaperId());
            List<QuestionCollect>questionCollectList = questionCollectMapper.selectList(quesQueryWrapper);

            Integer AnswerRatio = questionCollectList.size();
            Integer CorrectionProgress = 0;
            float Score = 0;
            for (QuestionCollect questionCollect : questionCollectList) {
                if(questionCollect.getCollectScore()!=0){
                    CorrectionProgress++;
                    Score+=questionCollect.getCollectScore();
                }
            }


            correctProgressVo temp = new correctProgressVo();
            temp.setUserId(paper.getPaperLink());
            User user = sysUserService.getUserById(paper.getPaperUser());
            temp.setUserNickname(user.getUserNickname());

            temp.setAnswerRatio((float) AnswerRatio/questionNum);
            temp.setCorrectionProgress((float) CorrectionProgress/questionNum);

            temp.setFullScore(fullScore);
            temp.setScore(Score);

            correctProgressVos.add(temp);
        }
        return Result.success(correctProgressVos);
    }

    @Override
    public Result quesCorrectProgress(String quesId, String examId,String authHeader) {
        //auth

        LambdaQueryWrapper<Paper> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Paper::getPaperLink,examId);
        List<Paper> paperList = paperMapper.selectList(queryWrapper);
        float num = paperList.size();

        float corrected = 0;
        LambdaQueryWrapper<QuestionCollect> quesQueryWrapper = new LambdaQueryWrapper<>();
        quesQueryWrapper.eq(QuestionCollect::getCollectLinkQuestion,quesId);
        List<QuestionCollect>questionCollectList = questionCollectMapper.selectList(quesQueryWrapper);
        for (QuestionCollect questionCollect : questionCollectList) {
            if(questionCollect.getCollectScore()!=null){
                corrected++;
            }
        }

        return Result.success(corrected/num);
    }

    @Override
    public Result getAnswerList(String quesId, String examId, String authHeader) {
        //auth

        LambdaQueryWrapper<Paper> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Paper::getPaperLink,examId);
        List<Paper> paperList = paperMapper.selectList(queryWrapper);

        LambdaQueryWrapper<QuestionCollect> quesQueryWrapper = new LambdaQueryWrapper<>();
        quesQueryWrapper.eq(QuestionCollect::getCollectLinkQuestion,quesId);
        List<QuestionCollect>questionCollectList = questionCollectMapper.selectList(quesQueryWrapper);

        List<AnswerSituationVo> answerSituationVoList = new ArrayList<>();
        for(Paper paper : paperList){
            AnswerSituationVo answerSituationVo = new AnswerSituationVo();
            answerSituationVo.setUserId(paper.getPaperUser());

            QuestionCollect findQues = null;
            for (QuestionCollect questionCollect : questionCollectList) {
                if(Objects.equals(questionCollect.getCollectLinkPaper(), paper.getPaperId()) && Objects.equals(questionCollect.getCollectLinkQuestion(), quesId)){
                    findQues = new QuestionCollect();
                    BeanUtils.copyProperties(questionCollect,findQues);
                    break;
                }
            }

            if (findQues == null) {
                //未答题
                answerSituationVo.setAnswerSituation("0");
            }
            else{
                //已答题

                if(findQues.getCollectScore()!=null){
                    //打过分
                    answerSituationVo.setAnswerSituation("2");
                    answerSituationVo.setScore(findQues.getCollectScore()+"");
                }else {
                    //未打分
                    answerSituationVo.setAnswerSituation("1");
                }

                answerSituationVo.setAnswer(findQues.getCollectText());
                answerSituationVo.setCollectId(findQues.getCollectId());
            }

            answerSituationVoList.add(answerSituationVo);
        }

        return Result.success(answerSituationVoList);
    }

    @Override
    public Result updateScore(UpdateScore updateScore, String authHeader) {
        QuestionCollect questionCollect = new QuestionCollect();
        if(updateScore.getCollectId()!=null){
            //存在记录
            questionCollect.setCollectId(updateScore.getCollectId());
            questionCollect.setCollectScore(Float.parseFloat(updateScore.getNewScore()));

            questionCollectMapper.updateById(questionCollect);
        }else {
            //不存在记录
            questionCollect.setCollectScore(Float.parseFloat(updateScore.getNewScore()));
            questionCollect.setCollectLinkQuestion(updateScore.getQuesId());
            questionCollect.setCollectText("");

            LambdaQueryWrapper<Paper> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Paper::getPaperUser,updateScore.getUserId());
            queryWrapper.eq(Paper::getPaperLink,updateScore.getExamId());

            questionCollect.setCollectLinkPaper(paperMapper.selectOne(queryWrapper).getPaperId());

            questionCollectMapper.insert(questionCollect);
        }
        return Result.success("成功");
    }

    @Override
    public Result calculateScore(String examId, String authHeader) {
        float fullScore = questionService.getFullScore(examId);
        LambdaQueryWrapper<Paper> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Paper::getPaperLink,examId);
        List<Paper> paperList = paperMapper.selectList(queryWrapper);

        for(Paper paper : paperList){
            LambdaQueryWrapper<QuestionCollect> quesQueryWrapper = new LambdaQueryWrapper<>();
            quesQueryWrapper.eq(QuestionCollect::getCollectLinkPaper,paper.getPaperId());
            List<QuestionCollect>questionCollectList = questionCollectMapper.selectList(quesQueryWrapper);

            float Score = 0;
            for (QuestionCollect questionCollect : questionCollectList) {
                if(questionCollect.getCollectScore()!=0){
                    Score+=questionCollect.getCollectScore();
                }
            }

            paper.setPaperScoreFull(fullScore);
            paper.setPaperScoreActual(Score);

            paperMapper.updateById(paper);
        }
        return Result.success("成功");
    }


    boolean judgeQues(String text,String answer){
        String[] textSplit  = text.split("-");
        String[] answerSplit = answer.split("-");

        if(textSplit.length == answerSplit.length){

            for (int i = 0; i < answerSplit.length; i++) {
                boolean flag = false;

                for (int j = 0; j < textSplit.length; j++){
                    if (Objects.equals(textSplit[j], answerSplit[i])) {
                        flag = true;
                        break;
                    }
                }

                if (!flag) {
                    return false;
                }
            }
            return true;

        }
    return false;
    }

    void setScore(String quesCollectId,Float score){
        QuestionCollect questionCollect = new QuestionCollect();
        questionCollect.setCollectId(quesCollectId);
        questionCollect.setCollectScore(score);

        questionCollectMapper.updateById(questionCollect);
    }
}
