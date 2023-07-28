package com.example.multifactorauth.exception;

import com.example.multifactorauth.payload.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(CustomException ex){
        ex.printStackTrace();
        return ResponseEntity.status(ex.getHttpStatus()).body(new ApiResponse(false, ex.getCode(), ex.getMessage()));
    }
}
