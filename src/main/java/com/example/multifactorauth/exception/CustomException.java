package com.example.multifactorauth.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomException extends RuntimeException{
    private String code;
    private String message;
    private HttpStatus httpStatus;

    public CustomException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public CustomException(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

}
