package com.example.multifactorauth.enums;

public enum RoleEnum {
    ROLE_ADMIN("Admin"),
    ROLE_CUSTOMER("Customer"),;

    private final String value;
    RoleEnum(String value) {
        this.value = value;

    }
    public String getValue(){
        return value;
    }

}
