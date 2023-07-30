package com.example.multifactorauth.service.impl;

import com.example.multifactorauth.config.security.JwtTokenUtil;
import com.example.multifactorauth.entity.User;
import com.example.multifactorauth.exception.CustomException;
import com.example.multifactorauth.payload.request.CodeRequest;
import com.example.multifactorauth.payload.request.LoginRequest;
import com.example.multifactorauth.payload.response.ApiResponse;
import com.example.multifactorauth.repository.UserRepository;
import com.example.multifactorauth.service.CustomUserDetail;
import com.example.multifactorauth.service.CustomUserDetailService;
import com.example.multifactorauth.service.TotpManager;
import com.example.multifactorauth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final TotpManager totpManager;
    private final CustomUserDetailService userDetailService;
    private final UserRepository userRepository;
    @Override
    public ApiResponse authenticateUser(LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        CustomUserDetail userDetail = userDetailService.loadUserByUsername(loginRequest.getEmail());
        if (userDetail.getUser().isEnableMfa()){
            String secretCode = totpManager.getCode();
            String qrCode = totpManager.getUriForImage(secretCode);
            // sending direct TOTP code for user to login
           LOGGER.info("TOTP CODE: {}", totpManager.generateTotp());
           String topt = totpManager.generateTotp();
           StringBuilder sb = new StringBuilder();
           String message = String.valueOf(sb.append("Your otp is: ").append(topt).append(" either use this otp or you can also access totp code on google authenticator to access code via authentication scan QR on authenticator. Code will be expired within 2 minute"));
           return new ApiResponse(qrCode, message);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenUtil.generateToken(authentication);
    }

    @Override
    public ApiResponse verify(CodeRequest codeRequest) {
        User user = userRepository.findByEmail(codeRequest.getEmail());
        if (user == null){
            throw new CustomException(HttpStatus.UNAUTHORIZED, "001", "Invalid credentials");
        }

        if(!totpManager.verifyCode(codeRequest.getCode(), user.getSecret())) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "T001","Code is incorrect");
        }

        CustomUserDetail customUserDetail = new CustomUserDetail(user);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(customUserDetail, null, customUserDetail.getAuthorities());
        ApiResponse apiResponse = jwtTokenUtil.generateToken(authenticationToken);
        return apiResponse;
    }
}
