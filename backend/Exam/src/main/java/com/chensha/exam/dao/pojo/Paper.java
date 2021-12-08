package com.chensha.exam.dao.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Paper {
    /**
     * 试卷id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String paperId;

    /**
     * 试卷答题人
     */
    private String paperUser;

    /**
     * 试卷所属的考试
     */
    private String paperLink;

    /**
     * 参加考试的时间，填写后代表已经参加考试
     */
    private Long paperTimeJoin;

    /**
     * 提交答案时间，填写后代表已经提交答案
     */
    private Long paperTimeUpdate;

    /**
     * 满分
     */
    private Integer paperScoreFull;

    /**
     * 得分
     */
    private Integer paperScoreActual;
}
