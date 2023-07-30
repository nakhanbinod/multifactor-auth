package com.example.multifactorauth.service;

public interface TotpManager {
    public void generateSecret();
    public String getUriForImage(String secret);
    public boolean verifyCode(String code, String secret);
    public String getCode();

    public String generateTotp();


}
