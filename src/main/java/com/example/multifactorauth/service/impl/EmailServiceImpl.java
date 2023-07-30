package com.example.multifactorauth.service.impl;

import com.example.multifactorauth.exception.CustomException;
import com.example.multifactorauth.payload.request.EmailRequest;
import com.example.multifactorauth.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Value("${spring.mail.username}")
    private String from;
    @Override
    public void sendVerificationEmail(EmailRequest emailRequest) throws CustomException {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(from);
            mailMessage.setTo(emailRequest.getRecipient());
            mailMessage.setText(emailRequest.getBody());
            mailMessage.setSubject(emailRequest.getSubject());
            javaMailSender.send(mailMessage);
            LOGGER.info("Mail send successfully to: {}", emailRequest.getRecipient());
        }catch (MailException ex){
            throw new CustomException(HttpStatus.BAD_REQUEST, "ERROR", ex.getMessage());
        }

    }
}
