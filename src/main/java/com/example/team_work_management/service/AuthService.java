package com.example.team_work_management.service;

import com.example.team_work_management.dto.PasswordChangeRequest;
import com.example.team_work_management.entity.Group;
import com.example.team_work_management.entity.User;

import java.io.IOException;
import java.util.List;

public interface AuthService {
    boolean register(User user);
    void retrieveCode(User user);
    boolean isVerificationCodeValid(User user) throws IOException;
    boolean login(User user);
    User getViewDetail(Long id);
    boolean changePassword(PasswordChangeRequest passwordChangeRequest);
    void generatePasswordResetCode(User user);
    boolean restPassword(User user);
    boolean updateUser(User user);
    User getCurrentAuthenticatedUser();
    User getDetail(Long id);
    List<User> searchByEmailAndGroup(String key, Long groupId);
}