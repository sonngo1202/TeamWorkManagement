package com.example.team_work_management.security;

import org.springframework.security.core.userdetails.User;

import java.util.Collections;

public class CustomUserDetails extends User {
    public CustomUserDetails(String email, String password){
        super(email, password, Collections.emptyList());
    }
}
