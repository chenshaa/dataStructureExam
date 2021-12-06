package com.chensha.exam.dao.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Exam {

    private static final long serialVersionUID=1L;

    /**
     * 主id
     */
    @TableId(type = IdType.ASSIGN_ID)
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
    private Long examCreateTime;

    /**
     * 考试更新时间
     */
    private Long examUpdateTime;

    /**
     * 考试开始时间
     */
    private Long examStartTime;

    /**
     * 考试结束时间
     */
    private Long examEndTime;


}
