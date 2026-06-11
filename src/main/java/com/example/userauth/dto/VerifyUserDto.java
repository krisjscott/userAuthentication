package com.example.userauth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyUserDto {
    private String email;
    private String verification;

    public String getVerificationCode() {
        return verification;
    }
}
