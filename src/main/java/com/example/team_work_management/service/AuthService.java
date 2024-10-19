package com.example.team_work_management.service;

import com.example.team_work_management.dto.PasswordChangeRequest;
import com.example.team_work_management.entity.User;

import java.io.IOException;

public interface AuthService {
    boolean register(User user);
    void retrieveCode(User user);
    boolean isVerificationCodeValid(User user) throws IOException;
    boolean login(User user);
    boolean changePassword(PasswordChangeRequest passwordChangeRequest);
    void generatePasswordResetCode(User user);
    boolean restPassword(User user);
    boolean updateUser(User user);
    User getCurrentAuthenticatedUser();
}