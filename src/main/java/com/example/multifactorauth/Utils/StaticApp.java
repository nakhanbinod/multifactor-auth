package com.example.multifactorauth.Utils;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;
import org.apache.commons.codec.binary.Base32;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class StaticApp {

        private final static Logger LOGGER = LoggerFactory.getLogger(StaticApp.class);
        public static String generateTotpCode(String secret){
            byte[] decodedKey = new Base32().decode(secret);
            GoogleAuthenticatorConfig config = new GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder().build();
            GoogleAuthenticator gAuth = new GoogleAuthenticator(config);
            int totp = gAuth.getTotpPassword(secret);
            return String.format("%06d", totp); // Return

        }
}
