package com.nhnacademy.spring.todo.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Event implements Serializable {

    private Long id;
    private String userId;
    private String subject;
    private String eventAt;
    private LocalDateTime createdAt;

    public Event(String userId,String subject,String eventAt) {
        this.userId = userId;
        this.subject = subject;
        this.eventAt = eventAt;
        this.createdAt = LocalDateTime.now();
    }

    public String getUserId() {
        return userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getEventAt() {
        return eventAt;
    }

    public void setEventAt(String eventAt) {
        this.eventAt = eventAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
