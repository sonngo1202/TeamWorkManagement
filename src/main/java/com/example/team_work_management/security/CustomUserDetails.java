package com.example.team_work_management.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUserDetails extends User {
    public CustomUserDetails(String email, String password, Collection<? extends GrantedAuthority> authorities){
        super(email, password, authorities);
    }
}
