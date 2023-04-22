package com.nhnacademy.spring.todo.exception;

import java.util.NoSuchElementException;

public class NotFoundEventException extends NoSuchElementException {
   private String errorMessage;
    public NotFoundEventException(long id) {
       this.errorMessage = "eventId : " + id;
    }
    public String getErrorMessage() {
        return errorMessage;
    }
}
