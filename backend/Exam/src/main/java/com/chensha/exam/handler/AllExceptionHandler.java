package com.chensha.exam.handler;

import com.chensha.exam.vo.ErrorCode;
import com.chensha.exam.vo.Result;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class AllExceptionHandler {

    @ExceptionHandler(MalformedJwtException.class)
    public Result handleExceptionHandler(Exception e){
        e.printStackTrace();
        return Result.fail(ErrorCode.ERROR_CODE.getCode(), ErrorCode.ERROR_CODE.getMsg());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public Result handleExpiredJwtException(ExpiredJwtException e) {
        e.printStackTrace();
        return Result.fail(ErrorCode.NO_PERMISSION.getCode(), ErrorCode.NO_PERMISSION.getMsg());
    }



    @ExceptionHandler(Exception.class)
    @ResponseBody //返回json数据
    public Result doException(Exception ex){
        ex.printStackTrace();
        //return Result.fail(ErrorCode.NO_PERMISSION.getCode(), ErrorCode.NO_PERMISSION.getMsg());
        return Result.fail(-999,"系统异常");
    }
}
