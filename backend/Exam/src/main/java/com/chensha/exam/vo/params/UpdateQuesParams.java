package com.chensha.exam.vo.params;

import lombok.Data;

@Data
public class UpdateQuesParams {

    private String questionId;

    private String examId;

    private String questionAnswer;
}
