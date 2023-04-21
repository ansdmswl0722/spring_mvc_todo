package com.nhnacademy.spring.todo.advice;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice
public class CommonControllerAdvice {
    @InitBinder
    void initBinder(WebDataBinder binder){
        binder.initDirectFieldAccess();
    }
}
