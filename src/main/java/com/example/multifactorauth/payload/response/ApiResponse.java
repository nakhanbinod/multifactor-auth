package com.example.multifactorauth.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {

    private boolean success = true;
    private String code;
    private String message;
    private Object result;

    public ApiResponse(boolean success, String code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public ApiResponse(Object result) {
        this.result = result;
    }

    public ApiResponse(Object result, String message) {
        this.result = result;
        this.message = message;
    }
}
