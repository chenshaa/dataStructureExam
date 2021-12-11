package com.chensha.exam.vo;

public enum ErrorCode {

    ERROR_CODE(400,"错误"),
    NO_PERMISSION(401,"无访问权限"),
    ERROR_PARAMETER(452,"参数错误"),
    ERROR_USERNAME(453,"用户名/密码错误"),
    ERROR_EXAM(403,"考试已结束/未开始"),
    OBJECT_MISSED(404,"不存在的对象"),
    OBJECT_EXISTS(409,"对象已经存在");

    private int code;
    private String msg;

    ErrorCode(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
