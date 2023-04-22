package com.nhnacademy.spring.todo.filter;

import com.nhnacademy.spring.todo.exception.InvalidOwnerException;
import com.nhnacademy.spring.todo.exception.MissingUserHeaderException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public class UserInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId = request.getHeader("X-USER-ID");
        if(Objects.nonNull(userId)){
            if(!userId.equals("marco")){
                throw new InvalidOwnerException();
            }
            return true;
        }else {
            throw new MissingUserHeaderException();
        }
    }
}
