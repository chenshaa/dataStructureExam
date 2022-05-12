package com.chensha.exam.vo.params;

import lombok.Data;

@Data
public class LinkQuesParams {
    /**
     * 试题ID
     */
    private String questionId;

    /**
     * 试题关联的试卷
     */
    private String questionLink;
}
