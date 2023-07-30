package com.example.multifactorauth.controller;

import com.example.multifactorauth.payload.request.RegisterRequest;
import com.example.multifactorauth.payload.response.ApiResponse;
import com.example.multifactorauth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @GetMapping("welcome")
    public ResponseEntity<?> welcom(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ApiResponse apiResponse = new ApiResponse(true, "0", "Welcome, " + authentication.getName());
        return ResponseEntity.ok().body(apiResponse);
    }

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest){
        ApiResponse apiResponse = userService.register(registerRequest);
        return ResponseEntity.ok().body(apiResponse);
    }

    @GetMapping("verify/account")
    public ResponseEntity<?> verifyAccount(@RequestParam("token") String token){
        ApiResponse apiResponse = userService.accountVerification(token);
        return ResponseEntity.ok().body(apiResponse);
    }
}
