package com.example.multifactorauth.service.impl;

import com.example.multifactorauth.config.security.JwtTokenUtil;
import com.example.multifactorauth.entity.Role;
import com.example.multifactorauth.entity.Token;
import com.example.multifactorauth.entity.User;
import com.example.multifactorauth.exception.CustomException;
import com.example.multifactorauth.payload.request.CodeRequest;
import com.example.multifactorauth.payload.request.EmailRequest;
import com.example.multifactorauth.payload.request.LoginRequest;
import com.example.multifactorauth.payload.request.RegisterRequest;
import com.example.multifactorauth.payload.response.ApiResponse;
import com.example.multifactorauth.repository.RoleRepository;
import com.example.multifactorauth.repository.TokenRepository;
import com.example.multifactorauth.repository.UserRepository;
import com.example.multifactorauth.service.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final TotpManager totpManager;
    private final CustomUserDetailService userDetailService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final TokenRepository tokenRepository;
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
        return jwtTokenUtil.generateToken(authenticationToken);
    }

    @Override
    public ApiResponse register(RegisterRequest registerRequest) {
        Role role = roleRepository.findById(registerRequest.getRole()).orElseThrow(()
                -> new CustomException(HttpStatus.NOT_FOUND, "404", "Role not found."));
       if (userRepository.existsByEmail(registerRequest.getEmail())){
           throw new CustomException(HttpStatus.CONFLICT, "100", "Email should be unique");
       }
       User user = new ModelMapper().map(registerRequest, User.class);
       user.setPassword(new BCryptPasswordEncoder().encode(registerRequest.getPassword()));
       user.setRole(role);
       userRepository.save(user);
       // send verfication link here
        Token token = new Token(user);
        tokenRepository.save(token);
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setSubject("Account verification link");
        emailRequest.setRecipient(user.getEmail());
        emailRequest.setBody("Please click below link for your account verification.: " + "http://localhost:8085/verify/account?token="+token.getVerificationToken());
        LOGGER.info("SENDING ACCOUNT VERIFICATION LINK TO: {}", user.getEmail());
        LOGGER.info("VERIFICATION LINK: {}", "http://localhost:8000/verify/account?token="+token.getVerificationToken()) ;
        emailService.sendVerificationEmail(emailRequest);
        return new ApiResponse(true, "0", "Account created success.");

    }

    @Override
    public ApiResponse accountVerification(String token) {
        Token verificationToken = tokenRepository.findByVerificationToken(token);
        if (verificationToken != null){
            User user  = userRepository.findByEmail(verificationToken.getUser().getEmail());
            user.setEnable(true);
            userRepository.save(user);
            return new ApiResponse(true, "0", "Account verification success.");
        }
        return new ApiResponse(false, "100", "Account verification failed");
    }

}
