package com.chensha.exam.vo;

import lombok.Data;

@Data
public class correctProgressVo {
    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户昵称
     */
    private String userNickname;

    /**
     * 答题比例
     */
    private Float answerRatio;

    /**
     * 批改比例
     */
    private Float CorrectionProgress;

    /**
     * 满分
     */
    private Float fullScore;

    /**
     * 得分
     */
    private Float score;
}
