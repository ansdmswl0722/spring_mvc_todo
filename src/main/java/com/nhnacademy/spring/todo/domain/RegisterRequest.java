package com.nhnacademy.spring.todo.domain;

import lombok.Value;

@Value
public class RegisterRequest {

    private String subject;
    private String eventAt;
}
