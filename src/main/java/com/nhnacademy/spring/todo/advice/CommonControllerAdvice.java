package com.nhnacademy.spring.todo.advice;

import com.nhnacademy.spring.todo.domain.ErrorResponse;
import com.nhnacademy.spring.todo.exception.InvalidOwnerException;
import com.nhnacademy.spring.todo.exception.MissingUserHeaderException;
import com.nhnacademy.spring.todo.exception.NotFoundEventException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestControllerAdvice
public class CommonControllerAdvice {
    @InitBinder
    void initBinder(WebDataBinder binder){
        binder.initDirectFieldAccess();
    }
    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> missingParameter(MissingServletRequestParameterException e){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(400)
                .timestamp(LocalDateTime.now())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {MissingUserHeaderException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> missingUserId(){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(401)
                .timestamp(LocalDateTime.now())
                .message("Unauthorized")
                .build();
        return new ResponseEntity<>(errorResponse,HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {InvalidOwnerException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorResponse> invalidEventOwner(){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(403)
                .timestamp(LocalDateTime.now())
                .message("잘못된 이벤트 소유자")
                .build();
        return new ResponseEntity<>(errorResponse,HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {NotFoundEventException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> notFoundEvent(NotFoundEventException e){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(404)
                .timestamp(LocalDateTime.now())
                .message("이벤트가 존재하지 않습니다. " + e.getErrorMessage())
                .build();
        return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseEntity<ErrorResponse> methodNotAllowed(HttpServletRequest request){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(405)
                .error(HttpStatus.METHOD_NOT_ALLOWED.toString())
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(errorResponse,HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> internalServlerError(MethodArgumentTypeMismatchException e){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(500)
                .timestamp(LocalDateTime.now())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse,HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
