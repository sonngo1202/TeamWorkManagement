package com.example.team_work_management.service;

import com.example.team_work_management.entity.User;

import java.io.IOException;

public interface AuthService {
    boolean register(User user);
    boolean retrieveCode(User user);
    boolean isVerificationCodeValid(User user) throws IOException;
    boolean login(User user);
}
