package com.chensha.exam.dao.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Question {
    private static final long serialVersionUID=1L;

    /**
     * 题目d
     */
    @TableId(type = IdType.ASSIGN_ID)
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
     * 题目属于哪一张试卷
     */
    private String questionLink;

    /**
     * 题目的第一个选项，可以为空
     */
    @TableField(value = "question_opinion_1")
    private String questionOpinion1;

    @TableField(value = "question_opinion_2")
    private String questionOpinion2;

    @TableField(value = "question_opinion_3")
    private String questionOpinion3;

    @TableField(value = "question_opinion_4")
    private String questionOpinion4;

    @TableField(value = "question_opinion_5")
    private String questionOpinion5;

    /**
     * 选择类为选项编号，用-连接；填空，简答选择第一个选项。内容未null表示无法自动判断。
     */
    private String questionRightChoice;

    /**
     * 题目创建时间
     */
    private Long questionCreateTime;

    /**
     * 题目更新时间
     */
    private Long questionUpdateTime;


}
