package com.nhnacademy.spring.todo.domain;

import lombok.Setter;
import lombok.Value;


public class RegisterResponse {

    private long id;

    public RegisterResponse(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

}
