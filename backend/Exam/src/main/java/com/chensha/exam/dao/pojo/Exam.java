package com.chensha.exam.dao.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Exam {

    private static final long serialVersionUID=1L;

    /**
     * 主id
     */
    private String examId;

    /**
     * 考试名称
     */
    private String examName;

    /**
     * 考试描述
     */
    private String examDescription;

    /**
     * 考试创建时间
     */
    private LocalDateTime examCreateTime;

    /**
     * 考试更新时间
     */
    private LocalDateTime examUpdateTime;

    /**
     * 考试开始时间
     */
    private LocalDateTime examStartTime;

    /**
     * 考试结束时间
     */
    private LocalDateTime examEndTime;


}
