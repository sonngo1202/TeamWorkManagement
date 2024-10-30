package com.example.team_work_management.security;

import lombok.Getter;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

public class CustomUserDetails extends User {
    @Getter
    private Long id;

    public CustomUserDetails(Long id ,String email, String password){
        super(email, password, Collections.emptyList());
        this.id = id;
    }

}
