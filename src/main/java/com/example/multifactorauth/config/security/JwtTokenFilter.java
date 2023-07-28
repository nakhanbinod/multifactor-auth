package com.example.multifactorauth.config.security;

import com.example.multifactorauth.exception.CustomException;
import com.example.multifactorauth.payload.response.ApiResponse;
import com.example.multifactorauth.service.CustomUserDetail;
import com.example.multifactorauth.service.CustomUserDetailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.swing.undo.CannotUndoException;
import java.io.IOException;
import java.rmi.server.ExportException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private CustomUserDetailService userDetailService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestTokenHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;
        try {
            if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
                jwtToken = requestTokenHeader.substring(7);
                try {
                    username = jwtTokenUtil.getUsernameFromToken(jwtToken);
                } catch (IllegalArgumentException e) {
                    throw new CustomException("003", "Unable to get JWT Token");
                } catch (ExpiredJwtException e) {
                    throw new CustomException("004", "JWT Token has expired");
                }
            } else {

            }
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                CustomUserDetail userDetails = userDetailService.loadUserByUsername(username);
                if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }catch (Exception ex){
            handleInvalidCredentialException(response, ex.getMessage());
        }
        filterChain.doFilter(request, response);
    }


    private void handleInvalidCredentialException(HttpServletResponse response, String errorMessage) throws io.jsonwebtoken.io.IOException, java.io.IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Map<String, Object> map = new HashMap<>();
        map.put("success", false);
        map.put("code", "005");
        map.put("message", errorMessage);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), map);
    }
}

