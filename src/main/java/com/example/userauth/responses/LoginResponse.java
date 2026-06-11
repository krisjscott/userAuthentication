package com.example.userauth.responses;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class LoginResponse {
    private String token;
    private long expiresIn;
    private boolean otpRequired;
    public LoginResponse(String token, long expiresIn, boolean otpRequired) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.otpRequired =  otpRequired;
    }



}
