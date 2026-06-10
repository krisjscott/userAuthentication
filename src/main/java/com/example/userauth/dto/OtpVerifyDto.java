package com.example.userauth.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


@Data
public class OtpVerifyDto {
    @NotNull
    @Size(min = 6, max = 6)
    private String otp;

    @NotNull
    private String email;
}
