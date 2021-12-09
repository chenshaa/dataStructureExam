package com.chensha.exam.vo;

import lombok.Data;

@Data
public class QuestionLiteVo {

    /**
     * 题目d
     */
    private String questionId;

    /**
     * 题目内容
     */
    private String questionText;

    /**
     * 题目图片，采用base64编码
     */
    private String questionPicture;

    /**
     * 题目分值
     */
    private Integer questionScore;

    /**
     * 题目类型，定义0为单选，1为多选，2为不定项选择，3为填空，4为判断，5为简答，6为综合题
     */
    private Integer questionType;

    /**
     * 题目的第一个选项，可以为空
     */
    private String questionOpinion1;

    private String questionOpinion2;

    private String questionOpinion3;

    private String questionOpinion4;

    private String questionOpinion5;

}
