package com.example.multifactorauth.config;

import com.example.multifactorauth.payload.response.ApiResponse;
import com.example.multifactorauth.service.CustomUserDetail;
import com.example.multifactorauth.service.CustomUserDetailService;
import com.example.multifactorauth.service.TotpManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthProvider implements AuthenticationProvider {
    private final static Logger LOGGER = LoggerFactory.getLogger(CustomAuthProvider.class);
    private final CustomUserDetailService userDetailService;
    private final TotpManager totpManager;

    @Override
    public Authentication authenticate(Authentication authentication) {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        CustomUserDetail customUserDetail = userDetailService.loadUserByUsername(username);
        try {
            if (!new BCryptPasswordEncoder().matches(password, customUserDetail.getPassword())){
                throw new BadCredentialsException("You've entered invalid password.");
            }
            if (Boolean.TRUE.equals(customUserDetail.getUser().isEnableMfa())){
                totpManager.generateSecret();
                String code = totpManager.getCode();
                LOGGER.info("SECRET CODE GENERATE FROM LOGIN METHOD: {}", code);
                userDetailService.updateSecret(code, customUserDetail.getUser().getEmail());
            }
        }catch (Exception ex){
            throw new BadCredentialsException(ex.getMessage());
        }
        return new UsernamePasswordAuthenticationToken(username, password, customUserDetail.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }


}
