package com.chensha.exam.handler;

import com.alibaba.fastjson.JSON;
import com.chensha.exam.dao.pojo.User;
import com.chensha.exam.service.UserService;
import com.chensha.exam.utils.JWTUtils;
import com.chensha.exam.vo.ErrorCode;
import com.chensha.exam.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    private final UserService userService;

    public LoginInterceptor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)){
            return true;
        }
        String authHeader = request.getHeader(JWTUtils.AUTH_HEADER_KEY);
        log.info("=================request start===========================");
        String requestURI = request.getRequestURI();
        log.info("request uri:{}",requestURI);
        log.info("request method:{}",request.getMethod());
        log.info("token:{}", authHeader);
        log.info("=================request end===========================");

        if (authHeader == null || !authHeader.startsWith(JWTUtils.TOKEN_PREFIX)){
            Result result = Result.fail(ErrorCode.NO_PERMISSION.getCode(), ErrorCode.NO_PERMISSION.getMsg());
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        String token = authHeader.substring(7);
        User user = userService.checkToken(token);
        if (user == null || org.apache.commons.lang3.StringUtils.isBlank(user.getUserAccount())){
            Result result = Result.fail(ErrorCode.NO_PERMISSION.getCode(), ErrorCode.NO_PERMISSION.getMsg());
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        //是登录状态，放行

        return true;
    }
}
