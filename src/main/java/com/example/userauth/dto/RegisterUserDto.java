package com.example.userauth.dto;

import com.example.userauth.model.Role;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RegisterUserDto {
    private String mail;
    private String password;
    private String username;
    private Role role;
}
