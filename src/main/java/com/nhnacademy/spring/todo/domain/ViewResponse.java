package com.nhnacademy.spring.todo.domain;

import lombok.Value;
import java.time.LocalDateTime;
@Value
public class ViewResponse {
    private long id;
    private String subject;
    private String eventAt;
    private LocalDateTime createdAt;
}
