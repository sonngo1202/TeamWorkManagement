package com.example.team_work_management.controller;

import com.example.team_work_management.config.Views;
import com.example.team_work_management.dto.PasswordChangeRequest;
import com.example.team_work_management.entity.User;
import com.example.team_work_management.security.CustomUserDetails;
import com.example.team_work_management.service.AuthService;
import com.example.team_work_management.service.TaskService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private TaskService taskService;

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
    @JsonView(Views.UserLogin.class)
    public ResponseEntity<?> login(@RequestBody User user){
        SecurityContextHolder.clearContext();
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

    @GetMapping("/{key}/group/{groupId}")
    @JsonView(Views.Summary.class)
    public ResponseEntity<?> searchByEmailAndGroup(@PathVariable Long groupId, @PathVariable String key){
        return ResponseEntity.ok(authService.searchByEmailAndGroup(key, groupId));
    }

    @GetMapping("/{id}")
    @JsonView(Views.UserDetailed.class)
    public ResponseEntity<?> getDetail(@PathVariable Long id){
        return ResponseEntity.ok(authService.getViewDetail(id));
    }

    @GetMapping("/tasks")
    @JsonView(Views.NotificationDetail.class)
    public ResponseEntity<?> getAllTaskOfUser(@AuthenticationPrincipal CustomUserDetails user){
        return ResponseEntity.ok(taskService.getByAssignee(user.getId()));
    }

    @GetMapping("/tasks/data-search")
    @JsonView(Views.NotificationDetail.class)
    public ResponseEntity<?> getTaskOfAllGroupUser(@AuthenticationPrincipal CustomUserDetails user){
        return ResponseEntity.ok(taskService.getAllByUserId(user.getId()));
    }

}
