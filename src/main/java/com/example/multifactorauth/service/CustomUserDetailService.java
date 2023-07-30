package com.example.multifactorauth.service;

import com.example.multifactorauth.config.security.JwtTokenUtil;
import com.example.multifactorauth.entity.User;
import com.example.multifactorauth.exception.CustomException;
import com.example.multifactorauth.payload.request.CodeRequest;
import com.example.multifactorauth.payload.request.LoginRequest;
import com.example.multifactorauth.payload.response.ApiResponse;
import com.example.multifactorauth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;


    @Override
    public CustomUserDetail loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null){
            throw new CustomException(HttpStatus.UNAUTHORIZED, "001", "Invalid credentials");
        }
        return new CustomUserDetail(user);
    }


    public void updateSecret(String secret, String email){
        userRepository.updateSecretToken(secret, email);
    }



}
