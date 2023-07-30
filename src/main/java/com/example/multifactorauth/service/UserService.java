package com.example.multifactorauth.service;

import com.example.multifactorauth.payload.request.CodeRequest;
import com.example.multifactorauth.payload.request.LoginRequest;
import com.example.multifactorauth.payload.request.RegisterRequest;
import com.example.multifactorauth.payload.response.ApiResponse;

public interface UserService {

    ApiResponse authenticateUser(LoginRequest loginRequest);
    ApiResponse verify(CodeRequest codeRequest);
    ApiResponse register(RegisterRequest registerRequest);
    ApiResponse accountVerification(String token);
}
