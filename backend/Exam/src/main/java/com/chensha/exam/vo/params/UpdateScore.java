package com.chensha.exam.vo.params;

import lombok.Data;

@Data
public class UpdateScore {

    private String examId;

    private String quesId;

    private String userId;

    private String collectId;

    private String newScore;
}
