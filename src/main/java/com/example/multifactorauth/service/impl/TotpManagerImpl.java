package com.example.multifactorauth.service.impl;

import com.example.multifactorauth.Utils.StaticApp;
import com.example.multifactorauth.exception.CustomException;
import com.example.multifactorauth.service.TotpManager;
import dev.samstevens.totp.code.*;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;


@Service
public class TotpManagerImpl implements TotpManager {

    private final static Logger LOGGER = LoggerFactory.getLogger(TotpManagerImpl.class);
    private String secretCode = null;
    @Override
    public void generateSecret() {
        SecretGenerator secretGenerator = new DefaultSecretGenerator();
        secretCode = secretGenerator.generate();
        LOGGER.info("GENERATING SECRET CODE: {}", secretCode);
    }

    @Override
    public String getCode(){
        return secretCode;

    }

    @Override
    public String generateTotp() {
        return StaticApp.generateTotpCode(secretCode);
    }

    @Override
    public String getUriForImage(String secret) {
        QrData data = new QrData.Builder()
                .label("2FA")
                .secret(secret)
                .issuer("mfa")
                .algorithm(HashingAlgorithm.SHA1)
                .digits(6)
                .period(30)
                .build();

        QrGenerator generator = new ZxingPngQrGenerator();
        byte[] imageData = new byte[0];

        try {
            imageData = generator.generate(data);
        } catch (QrGenerationException e) {
            throw new CustomException("Q001", "unable to generate QrCode");
        }

        String mimeType = generator.getImageMimeType();

        return getDataUriForImage(imageData, mimeType);
    }

    @Override
    public boolean verifyCode(String code, String secret) {
        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();
        CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
        return verifier.isValidCode(secret, code);
    }
}
