package com.example.multifactorauth.service;


import com.example.multifactorauth.exception.CustomException;
import com.example.multifactorauth.payload.request.EmailRequest;

public interface EmailService {

    void sendVerificationEmail(EmailRequest emailRequest) throws CustomException;
}
