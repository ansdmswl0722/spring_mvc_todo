package com.nhnacademy.spring.todo.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ErrorResponse {
    private int statusCode;
    private LocalDateTime timestamp;
    private String message;
    private String error;
    private String path;


}
