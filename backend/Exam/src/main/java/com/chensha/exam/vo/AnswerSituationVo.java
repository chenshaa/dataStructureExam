package com.chensha.exam.vo;

import lombok.Data;

@Data
public class AnswerSituationVo {
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 答题情况
     * 0：未答题 1：已答题未批改 2：已批改
     */
    private String answerSituation;

    /**
     *  收集ID
     */
    private String collectId;

    /**
     * 回答
     */
    private String answer;

    /**
     * 得分
     */
    private String score;
}
