package com.chensha.exam.vo.params;

import lombok.Data;

@Data
public class UserParams {
    private String token;

    private String account;

    private String userNickname;

    private String password;

    private Integer userGroup;
}
