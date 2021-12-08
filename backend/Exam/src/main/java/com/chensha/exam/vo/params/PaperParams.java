package com.chensha.exam.vo.params;

import lombok.Data;

@Data
public class PaperParams {

    /**
     * 试卷答题人
     */
    private String paperUser;

    /**
     * 试卷所属的考试
     */
    private String paperLink;
}
