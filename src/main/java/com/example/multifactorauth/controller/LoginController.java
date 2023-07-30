package com.example.multifactorauth.controller;

import com.example.multifactorauth.config.security.JwtTokenUtil;
import com.example.multifactorauth.payload.request.CodeRequest;
import com.example.multifactorauth.payload.request.LoginRequest;
import com.example.multifactorauth.payload.response.ApiResponse;
import com.example.multifactorauth.service.CustomUserDetailService;
import com.example.multifactorauth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/")
@RequiredArgsConstructor
public class LoginController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginRequest loginRequest) {
        ApiResponse apiResponse = userService.authenticateUser(loginRequest);
        return ResponseEntity.ok().body(apiResponse);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyCode(@RequestBody CodeRequest codeRequest) {
        ApiResponse token = userService.verify(codeRequest);
        return ResponseEntity.ok().body(token);
    }


}
