package com.example.multifactorauth.controller;

import com.example.multifactorauth.payload.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/")
public class UserController {


    @GetMapping("welcome")
    public ResponseEntity<?> welcom(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ApiResponse apiResponse = new ApiResponse(true, "0", "Welcome, " + authentication.getName());
        return ResponseEntity.ok().body(apiResponse);
    }
}
