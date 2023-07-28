package com.example.multifactorauth.enums;

public enum Status {
    ACTIVE("Active"),
    BLOCK("Block"),;

    private final String value;
    Status(String value) {
        this.value = value;
    }
    public String getValue(){
        return value;
    }
}
