package com.example.multifactorauth.config.security;

import com.example.multifactorauth.exception.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class CustomAuthEntryPoint implements AuthenticationEntryPoint {

    private final static Logger LOGGER = LoggerFactory.getLogger(CustomAuthEntryPoint.class);
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        LOGGER.error("UNAUTHORIZED ERROR: {}", authException.getMessage());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        final Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("code", "ERROR");
        result.put("message", authException.getMessage());
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), result);

    }
}
