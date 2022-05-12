package com.chensha.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.chensha.exam.dao.mapper.PaperMapper;
import com.chensha.exam.dao.mapper.QuestionCollectMapper;
import com.chensha.exam.dao.mapper.QuestionMapper;
import com.chensha.exam.dao.pojo.*;
import com.chensha.exam.service.*;
import com.chensha.exam.utils.JWTUtils;
import com.chensha.exam.vo.*;
import com.chensha.exam.vo.params.PaperParams;
import com.chensha.exam.vo.params.UpdateQuesParams;
import com.chensha.exam.vo.params.UpdateScore;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PaperServiceImpl implements PaperService {

    public final SysUserService sysUserService;
    public final PaperMapper paperMapper;
    public final SysPaperService sysPaperService;
    public final QuestionMapper questionMapper;
    public final QuestionCollectMapper questionCollectMapper;
    public final QuestionService questionService;
    public final ExamService examService;

    public PaperServiceImpl(SysUserService sysUserService, PaperMapper paperMapper,
                            SysPaperService sysPaperService, QuestionMapper questionMapper,
                            QuestionCollectMapper questionCollectMapper, QuestionService questionService,
                            ExamService examService) {
        this.sysUserService = sysUserService;
        this.paperMapper = paperMapper;
        this.sysPaperService = sysPaperService;
        this.questionMapper = questionMapper;
        this.questionCollectMapper = questionCollectMapper;
        this.questionService = questionService;
        this.examService = examService;
    }

    /**
     * 学生查询自己的考试
     *
     * @param authHeader token认证头
     * @return List<ExamVo> 学生自己的考试列表
     */
    @Override
    public Result listMyExam(String authHeader) {
        String account = JWTUtils.getAccount(authHeader.substring(7));
        if (account == null) {
            return Result.fail(ErrorCode.NO_PERMISSION.getCode(), ErrorCode.NO_PERMISSION.getMsg());
        }
        String userid = sysUserService.getIdByAccount(account);
        LambdaQueryWrapper<Paper> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Paper::getPaperUser, userid);
        List<Paper> paperList = paperMapper.selectList(queryWrapper);

        List<ExamVo> examVoList = new ArrayList<>();
        for (Paper paper : paperList) {
            ExamVo examVo = sysPaperService.getExamVoById(paper.getPaperLink());
            examVoList.add(examVo);
        }

        return Result.success(examVoList);
    }

    /**
     * 学生开始一场考试
     *
     * @param examId     考试id
     * @param authHeader token认证头
     * @return List<QuestionLiteVo> 学生试卷的题目列表
     */
    @Override
    public Result startExam(String examId, String authHeader) {
        if (StringUtils.isBlank(examId)) {
            return Result.fail(ErrorCode.ERROR_PARAMETER.getCode(), ErrorCode.ERROR_PARAMETER.getMsg());
        }

        String owner = JWTUtils.getAccount(authHeader.substring(7));
        String userId = sysUserService.getIdByAccount(owner);
        //用户不存在
        if (StringUtils.isBlank(owner)) {
            return Result.fail(ErrorCode.NO_PERMISSION.getCode(), ErrorCode.NO_PERMISSION.getMsg());
        }
        Paper paper = sysPaperService.getPaperByExamId(examId, userId);
        //试卷不存在
        if (paper == null) {
            return Result.fail(ErrorCode.ERROR_PARAMETER.getCode(), ErrorCode.ERROR_PARAMETER.getMsg());
        }

        //校验是否已经开始
        Exam exam = examService.getExamById(examId);
        if (exam == null) {
            return Result.fail(ErrorCode.ERROR_PARAMETER.getCode(), ErrorCode.ERROR_PARAMETER.getMsg());
        }
        //存在开始时间
        if (exam.getExamStartTime() != null) {
            //未到开始时间
            if (System.currentTimeMillis() < exam.getExamStartTime()) {
                return Result.fail(ErrorCode.ERROR_EXAM.getCode(), ErrorCode.ERROR_EXAM.getMsg());
            }
        }
        //存在结束时间
        if (exam.getExamEndTime() != null) {
            //已过结束时间
            if (System.currentTimeMillis() > exam.getExamEndTime()) {
                return Result.fail(ErrorCode.ERROR_EXAM.getCode(), ErrorCode.ERROR_EXAM.getMsg());
            }
        }

        //首次进入考试
        if (paper.getPaperTimeJoin() == null) {
            paper.setPaperTimeJoin(System.currentTimeMillis());
            paperMapper.updateById(paper);
        }

        return Result.success(sysPaperService.getQuesLiteVoByExamId(examId));
    }

    /**
     * 由管理员或教师添加一张试卷
     *
     * @param paperParams 试卷参数
     * @param authHeader  token认证头
     * @return 成功
     */
    @Override
    public Result addPaper(PaperParams paperParams, String authHeader) {
        //参数校验
        if (paperParams == null || StringUtils.isEmpty(paperParams.getPaperLink()) ||
                StringUtils.isEmpty(paperParams.getPaperUser())) {
            return Result.fail(ErrorCode.ERROR_PARAMETER.getCode(), ErrorCode.ERROR_PARAMETER.getMsg());
        }

        //权限校验
        if (sysUserService.authHeader4Admin(authHeader) == null) {
            return Result.fail(ErrorCode.NO_PERMISSION.getCode(), ErrorCode.NO_PERMISSION.getMsg());
        }

        Paper paper = new Paper();
        paper.setPaperUser(paperParams.getPaperUser());
        paper.setPaperLink(paperParams.getPaperLink());
        paperMapper.insert(paper);

        return Result.success("成功");
    }

    /**
     * 学生在开始考试后在有需要的情况下再次得到试卷
     * (将要废除的方法)
     *
     * @param examId     考试id
     * @param authHeader token认证头
     * @return List<QuestionLiteVo> 学生试卷的题目列表
     */
    @Override
    public Result getPaper(String examId, String authHeader) {
        return startExam(examId, authHeader);
    }

    /**
     * 学生回答一道问题
     *
     * @param updateQuesParams 回答问题参数
     * @param authHeader       token认证头
     * @return 成功
     */
    @Override
    public Result updateOne(UpdateQuesParams updateQuesParams, String authHeader) {
        //判空
        if (updateQuesParams.getQuestionId() == null || updateQuesParams.getQuestionAnswer() == null
                || updateQuesParams.getExamId() == null) {
            return Result.fail(ErrorCode.ERROR_PARAMETER.getCode(), ErrorCode.ERROR_PARAMETER.getMsg());
        }

        String userId = sysUserService.getIdByAccount(JWTUtils.getAccount(authHeader.substring(7)));
        String paperId = sysPaperService.getPaperIdByExamID(updateQuesParams.getExamId(), userId);
        if (paperId == null) {
            return Result.fail(ErrorCode.ERROR_PARAMETER.getCode(), ErrorCode.ERROR_PARAMETER.getMsg());
        }

        //判断超时
        if (!checkExamOutOfTime(updateQuesParams.getExamId())) {
            return Result.fail(ErrorCode.ERROR_EXAM.getCode(), ErrorCode.ERROR_EXAM.getMsg());
        }

        //是否已经提交过
        LambdaQueryWrapper<QuestionCollect> quesQueryWrapper = new LambdaQueryWrapper<>();
        quesQueryWrapper.eq(QuestionCollect::getCollectLinkQuestion, updateQuesParams.getQuestionId());
        quesQueryWrapper.eq(QuestionCollect::getCollectLinkPaper, paperId);
        QuestionCollect ques = questionCollectMapper.selectOne(quesQueryWrapper);
        if (ques == null) {
            //不存在记录
            QuestionCollect questionCollect = new QuestionCollect();
            questionCollect.setCollectLinkQuestion(updateQuesParams.getQuestionId());
            questionCollect.setCollectLinkPaper(paperId);
            questionCollect.setCollectText(updateQuesParams.getQuestionAnswer());

            questionCollectMapper.insert(questionCollect);
        } else {
            //存在记录
            ques.setCollectText(updateQuesParams.getQuestionAnswer());
            questionCollectMapper.updateById(ques);
        }

        return Result.success("成功");
    }

    /**
     * 自动批改试卷
     *
     * @param examId     考试id
     * @param authHeader 认证头
     * @return 成功
     */
    @Override
    public Result autoCorrect(String examId, String authHeader) {
        if (StringUtils.isBlank(examId)) {
            return Result.fail(ErrorCode.ERROR_PARAMETER.getCode(), ErrorCode.ERROR_PARAMETER.getMsg());
        }

        //认证
        if (sysUserService.authHeader4Admin(authHeader) == null) {
            return Result.fail(ErrorCode.NO_PERMISSION.getCode(), ErrorCode.NO_PERMISSION.getMsg());
        }

        //todo：升级redis
        //question: [questionType, questionFullMark, RightChoice]
        Map<String, List<String>> localAnswerMap = new HashMap<>();

        //得到总共的试题id列表
        LambdaQueryWrapper<Question> quesQueryWrapper = new LambdaQueryWrapper<>();
        quesQueryWrapper.eq(Question::getQuestionLink, examId);
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

        List<Paper> paperList = sysPaperService.getPaperListByExamId(examId);
        for (Paper paper : paperList) {
            //由试卷枚举个人已提交的记录
            LambdaQueryWrapper<QuestionCollect> lambdaQueryWrap = new LambdaQueryWrapper<>();
            lambdaQueryWrap.eq(QuestionCollect::getCollectLinkPaper, paper.getPaperId());
            List<QuestionCollect> quesList = questionCollectMapper.selectList(lambdaQueryWrap);

            //个人的题目列表
            List<String> personalQuesList = new ArrayList<>();
            for (QuestionCollect questionCollect : quesList) {
                personalQuesList.add(questionCollect.getCollectLinkQuestion());
            }

            for (String quesId : totalQuesList) {
                //初始化
                List<String> answerList = null;
                //本地数据集查询
                for (Map.Entry<String, List<String>> entry : localAnswerMap.entrySet()) {
                    if (entry.getKey().equals(quesId)) {
                        answerList = entry.getValue();
                    }
                }
                //题目类型
                String quesType = null;
                if (answerList != null) {
                    quesType = answerList.get(0);
                }
                //是否自动批改
                boolean autoCorrectSwitch = Objects.equals(quesType, "0") || Objects.equals(quesType, "1") || Objects.equals(quesType, "2");

                if (personalQuesList.contains(quesId)) {
                    //已答题

                    QuestionCollect ques = new QuestionCollect();
                    for (QuestionCollect queryQues : quesList) {
                        if (Objects.equals(queryQues.getCollectLinkQuestion(), quesId)) {
                            BeanUtils.copyProperties(queryQues, ques);
                            break;
                        }
                    }

                    if (autoCorrectSwitch) {
                        if (judgeQues(ques.getCollectText(), answerList.get(2))) {
                            //正确
                            int fullMark = Integer.parseInt(answerList.get(1));
                            setScore(ques.getCollectId(), (float) fullMark);
                        } else {
                            //错误
                            setScore(ques.getCollectId(), (float) 0);
                        }
                    }


                } else {
                    //未答题
                    if (autoCorrectSwitch) {
                        QuestionCollect questionCollect = new QuestionCollect();

                        questionCollect.setCollectScore((float) 0);
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

    /**
     * 列出参加考试的学生名单
     *
     * @param examId     考试id
     * @param authHeader 认证头
     * @return List<UserVo> 学生列表
     */
    @Override
    public Result listStu(String examId, String authHeader) {
        if (StringUtils.isBlank(examId)) {
            return Result.fail(ErrorCode.ERROR_PARAMETER.getCode(), ErrorCode.ERROR_PARAMETER.getMsg());
        }

        if (sysUserService.authHeader4Admin(authHeader) == null) {
            return Result.fail(ErrorCode.NO_PERMISSION.getCode(), ErrorCode.NO_PERMISSION.getMsg());
        }

        LambdaQueryWrapper<Paper> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Paper::getPaperLink, examId);
        List<Paper> paperList = paperMapper.selectList(queryWrapper);

        List<User> userList = new ArrayList<>();
        for (Paper paper : paperList) {
            userList.add(sysUserService.getUserById(paper.getPaperUser()));
        }

        return Result.success(sysUserService.copyList(userList));
    }

    /**
     * 返回考试情况
     *
     * @param examId     考试ID
     * @param authHeader 认证头
     * @return correctProgressVos 考试情况json
     */
    @Override
    public Result correctProgress(String examId, String authHeader) {
        if (StringUtils.isBlank(examId)) {
            return Result.fail(ErrorCode.ERROR_PARAMETER.getCode(), ErrorCode.ERROR_PARAMETER.getMsg());
        }

        if (sysUserService.authHeader4Admin(authHeader) == null) {
            return Result.fail(ErrorCode.NO_PERMISSION.getCode(), ErrorCode.NO_PERMISSION.getMsg());
        }

        List<correctProgressVo> correctProgressVos = new ArrayList<>();
        Integer questionNum = questionService.countQues(examId);
        Float fullScore = questionService.getFullScore(examId);

        LambdaQueryWrapper<Paper> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Paper::getPaperLink, examId);
        List<Paper> paperList = paperMapper.selectList(queryWrapper);

        for (Paper paper : paperList) {
            LambdaQueryWrapper<QuestionCollect> quesQueryWrapper = new LambdaQueryWrapper<>();
            quesQueryWrapper.eq(QuestionCollect::getCollectLinkPaper, paper.getPaperId());
            List<QuestionCollect> questionCollectList = questionCollectMapper.selectList(quesQueryWrapper);

            int AnswerRatio = questionCollectList.size();
            int CorrectionProgress = 0;
            float Score = 0;
            for (QuestionCollect questionCollect : questionCollectList) {
                if (questionCollect.getCollectScore() != null) {
                    CorrectionProgress++;
                    Score += questionCollect.getCollectScore();
                }
            }


            correctProgressVo temp = new correctProgressVo();
            temp.setUserId(paper.getPaperUser());
            User user = sysUserService.getUserById(paper.getPaperUser());
            temp.setUserNickname(user.getUserNickname());

            temp.setAnswerRatio((float) AnswerRatio / questionNum);
            temp.setCorrectionProgress((float) CorrectionProgress / questionNum);

            temp.setFullScore(fullScore);
            temp.setScore(Score);

            correctProgressVos.add(temp);
        }
        return Result.success(correctProgressVos);
    }

    /**
     * 由管理员或教师获取某一条道试题的批改进度
     *
     * @param quesId     题目id
     * @param examId     考试id
     * @param authHeader 认证头
     * @return 百分比，以小数表示
     */
    @Override
    public Result quesCorrectProgress(String quesId, String examId, String authHeader) {
        //auth
        if (sysUserService.authHeader4Admin(authHeader) == null) {
            return Result.fail(ErrorCode.NO_PERMISSION.getCode(), ErrorCode.NO_PERMISSION.getMsg());
        }

        if (StringUtils.isBlank(quesId) || StringUtils.isBlank(examId)) {
            return Result.fail(ErrorCode.ERROR_PARAMETER.getCode(), ErrorCode.ERROR_PARAMETER.getMsg());
        }

        LambdaQueryWrapper<Paper> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Paper::getPaperLink, examId);
        List<Paper> paperList = paperMapper.selectList(queryWrapper);
        float num = paperList.size();

        float corrected = 0;
        LambdaQueryWrapper<QuestionCollect> quesQueryWrapper = new LambdaQueryWrapper<>();
        quesQueryWrapper.eq(QuestionCollect::getCollectLinkQuestion, quesId);
        List<QuestionCollect> questionCollectList = questionCollectMapper.selectList(quesQueryWrapper);
        for (QuestionCollect questionCollect : questionCollectList) {
            if (questionCollect.getCollectScore() != null) {
                corrected++;
            }
        }

        return Result.success(corrected / num);
    }

    /**
     * 由管理员或教师获得全部学生对一道问题的回答
     *
     * @param quesId     题目id
     * @param examId     考试id
     * @param authHeader 认证头
     * @return List<AnswerSituationVo>
     */
    @Override
    public Result getAnswerList(String quesId, String examId, String authHeader) {
        //auth
        if (sysUserService.authHeader4Admin(authHeader) == null) {
            return Result.fail(ErrorCode.NO_PERMISSION.getCode(), ErrorCode.NO_PERMISSION.getMsg());
        }

        if (StringUtils.isBlank(quesId) || StringUtils.isBlank(examId)) {
            return Result.fail(ErrorCode.ERROR_PARAMETER.getCode(), ErrorCode.ERROR_PARAMETER.getMsg());
        }

        LambdaQueryWrapper<Paper> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Paper::getPaperLink, examId);
        List<Paper> paperList = paperMapper.selectList(queryWrapper);

        LambdaQueryWrapper<QuestionCollect> quesQueryWrapper = new LambdaQueryWrapper<>();
        quesQueryWrapper.eq(QuestionCollect::getCollectLinkQuestion, quesId);
        List<QuestionCollect> questionCollectList = questionCollectMapper.selectList(quesQueryWrapper);

        List<AnswerSituationVo> answerSituationVoList = new ArrayList<>();
        for (Paper paper : paperList) {
            AnswerSituationVo answerSituationVo = new AnswerSituationVo();
            answerSituationVo.setUserId(paper.getPaperUser());

            QuestionCollect findQues = null;
            for (QuestionCollect questionCollect : questionCollectList) {
                if (Objects.equals(questionCollect.getCollectLinkPaper(), paper.getPaperId())
                        && Objects.equals(questionCollect.getCollectLinkQuestion(), quesId)) {
                    findQues = new QuestionCollect();
                    BeanUtils.copyProperties(questionCollect, findQues);
                    break;
                }
            }

            if (findQues == null) {
                //未答题
                answerSituationVo.setAnswerSituation("0");
            } else {
                //已答题

                if (findQues.getCollectScore() != null) {
                    //打过分
                    answerSituationVo.setAnswerSituation("2");
                    answerSituationVo.setScore(findQues.getCollectScore() + "");
                } else {
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

    /**
     * 由管理员或教师为一条回答设置得分
     *
     * @param updateScore 参数
     * @param authHeader  认证头
     * @return 成功
     */
    @Override
    public Result updateScore(UpdateScore updateScore, String authHeader) {
        if (StringUtils.isBlank(updateScore.getExamId())) {
            return Result.fail(ErrorCode.ERROR_PARAMETER.getCode(), ErrorCode.ERROR_PARAMETER.getMsg());
        }

        //auth
        if (sysUserService.authHeader4Admin(authHeader) == null) {
            return Result.fail(ErrorCode.NO_PERMISSION.getCode(), ErrorCode.NO_PERMISSION.getMsg());
        }

        QuestionCollect questionCollect = new QuestionCollect();
        if (updateScore.getCollectId() != null) {
            //存在记录
            questionCollect.setCollectId(updateScore.getCollectId());
            questionCollect.setCollectScore(Float.parseFloat(updateScore.getNewScore()));

            questionCollectMapper.updateById(questionCollect);
        } else {
            //不存在记录
            questionCollect.setCollectScore(Float.parseFloat(updateScore.getNewScore()));
            questionCollect.setCollectLinkQuestion(updateScore.getQuesId());
            questionCollect.setCollectText("");

            LambdaQueryWrapper<Paper> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Paper::getPaperUser, updateScore.getUserId());
            queryWrapper.eq(Paper::getPaperLink, updateScore.getExamId());

            questionCollect.setCollectLinkPaper(paperMapper.selectOne(queryWrapper).getPaperId());

            questionCollectMapper.insert(questionCollect);
        }
        return Result.success("成功");
    }

    /**
     * 由管理员或教师发起，为一场考试自动计算成绩单
     *
     * @param examId     考试id
     * @param authHeader 认证头
     * @return 成功
     */
    @Override
    public Result calculateScore(String examId, String authHeader) {
        if (StringUtils.isBlank(examId)) {
            return Result.fail(ErrorCode.ERROR_PARAMETER.getCode(), ErrorCode.ERROR_PARAMETER.getMsg());
        }

        //auth
        if (sysUserService.authHeader4Admin(authHeader) == null) {
            return Result.fail(ErrorCode.NO_PERMISSION.getCode(), ErrorCode.NO_PERMISSION.getMsg());
        }

        float fullScore = questionService.getFullScore(examId);
        LambdaQueryWrapper<Paper> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Paper::getPaperLink, examId);
        List<Paper> paperList = paperMapper.selectList(queryWrapper);

        for (Paper paper : paperList) {
            LambdaQueryWrapper<QuestionCollect> quesQueryWrapper = new LambdaQueryWrapper<>();
            quesQueryWrapper.eq(QuestionCollect::getCollectLinkPaper, paper.getPaperId());
            List<QuestionCollect> questionCollectList = questionCollectMapper.selectList(quesQueryWrapper);

            float Score = 0;
            for (QuestionCollect questionCollect : questionCollectList) {
                if (questionCollect.getCollectScore() != null) {
                    Score += questionCollect.getCollectScore();
                }
            }

            paper.setPaperScoreFull(fullScore);
            paper.setPaperScoreActual(Score);

            paperMapper.updateById(paper);
        }
        return Result.success("成功");
    }

    boolean judgeQues(String text, String answer) {
        String[] textSplit = text.split("-");
        String[] answerSplit = answer.split("-");

        if (textSplit.length == answerSplit.length) {

            for (String s : answerSplit) {
                boolean flag = false;

                for (String value : textSplit) {
                    if (Objects.equals(value, s)) {
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

    void setScore(String quesCollectId, Float score) {
        QuestionCollect questionCollect = new QuestionCollect();
        questionCollect.setCollectId(quesCollectId);
        questionCollect.setCollectScore(score);

        questionCollectMapper.updateById(questionCollect);
    }

    boolean checkExamOutOfTime(String examId) {
        Exam exam = examService.getExamById(examId);
        if (exam == null) {
            return false;
        }
        //存在开始时间
        if (exam.getExamStartTime() != null) {
            //未到开始时间
            if (System.currentTimeMillis() < exam.getExamStartTime()) {
                return false;
            }
        }
        //存在结束时间
        if (exam.getExamEndTime() != null) {
            //已过结束时间
            return System.currentTimeMillis() <= exam.getExamEndTime();
        }
        return true;
    }
}

