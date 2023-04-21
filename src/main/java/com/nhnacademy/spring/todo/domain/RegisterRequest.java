package com.nhnacademy.spring.todo.domain;

import lombok.Value;

@Value
public class RegisterRequest {

    String subject;
    String eventAt;
}
