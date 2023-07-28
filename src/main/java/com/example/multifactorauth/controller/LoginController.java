package com.example.multifactorauth.controller;

import com.example.multifactorauth.config.security.JwtTokenUtil;
import com.example.multifactorauth.payload.request.LoginRequest;
import com.example.multifactorauth.payload.response.ApiResponse;
import com.example.multifactorauth.service.CustomUserDetailService;
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
    private final CustomUserDetailService userDetailService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()

                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final ApiResponse jwtResponse = jwtTokenUtil.generateToken(authentication);
        return ResponseEntity.ok().body(jwtResponse);
    }


}
