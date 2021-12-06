package com.chensha.exam.vo.params;

import lombok.Data;

@Data
public class ExamParams {
    /**
     * 考试名称
     */
    private String examName;

    /**
     * 考试描述
     */
    private String examDescription;

    /**
     * 考试开始时间
     */
    private Long examStartTime;

    /**
     * 考试结束时间
     */
    private Long examEndTime;
}
