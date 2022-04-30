package com.chensha.exam.vo;

import lombok.Data;

@Data
public class UserVo {
    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户登录账号
     */
    private String userAccount;

    /**
     * 用户昵称
     */
    private String userNickname;

    /**
     * 用户所属权限组
     */
    private Integer userGroup;
}
