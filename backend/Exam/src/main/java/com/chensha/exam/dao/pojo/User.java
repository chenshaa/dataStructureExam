package com.chensha.exam.dao.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class User {
    @TableId(type = IdType.ASSIGN_ID)

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
     * 用户密码
     */
    private String userPassword;

    /**
     * 用户所属权限组
     */
    private Integer userGroup;

    private Long userCreateTime;

    private Long userUpdateTime;

}
