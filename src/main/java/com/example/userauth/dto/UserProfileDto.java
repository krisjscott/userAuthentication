package com.example.userauth.dto;

import com.example.userauth.model.Role;
import lombok.Getter;

@Getter
public class
UserProfileDto {
    private final Long id;
    private final String username;
    private final String email;
    private final Role role;

    public UserProfileDto(Long id, String username, String email, Role role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
    }
}
