package com.example.team_work_management.controller;

import com.example.team_work_management.dto.PasswordChangeRequest;
import com.example.team_work_management.entity.User;
import com.example.team_work_management.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user){
        authService.register(user);
        return ResponseEntity.ok("Registration successful");
    }

    @PostMapping("/re-code")
    public ResponseEntity<?> reCode(@RequestBody User user){
        authService.retrieveCode(user);
        return ResponseEntity.ok("Retrieve Code successful");
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyCode(@RequestBody User user) throws IOException {
        authService.isVerificationCodeValid(user);
        return ResponseEntity.ok("Verify code successful");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user){
        authService.login(user);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest passwordChangeRequest){
        authService.changePassword(passwordChangeRequest);
        return ResponseEntity.ok("Change password successful");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody User user){
        authService.generatePasswordResetCode(user);
        return ResponseEntity.ok("Password reset code sent to your email");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody User user){
        authService.restPassword(user);
        return ResponseEntity.ok("Password reset successful");
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody User user){
        authService.updateUser(user);
        return ResponseEntity.ok("User updated successfully");
    }

}
