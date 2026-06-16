package com.example.userauth.dto;

import com.example.userauth.model.Role;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RegisterUserDto {
    private String email;
    private String password;
    private String username;
    private Role role;
}
