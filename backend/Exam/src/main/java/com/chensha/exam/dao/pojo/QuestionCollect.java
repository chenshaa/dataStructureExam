package com.chensha.exam.dao.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class QuestionCollect {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String collectId;

    /**
     * 关联到的试卷
     */
    private String collectLinkPaper;

    /**
     * 关联到的题目
     */
    private String collectLinkQuestion;

    /**
     * 答题内容
     */
    private String collectText;

    /**
     * 得分
     */
    private Float collectScore;
}
