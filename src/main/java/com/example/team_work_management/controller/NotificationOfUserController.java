package com.example.team_work_management.controller;

import com.example.team_work_management.config.Views;
import com.example.team_work_management.security.CustomUserDetails;
import com.example.team_work_management.service.NotificationOfUserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class NotificationOfUserController {
    @Autowired
    private NotificationOfUserService notificationOfUserService;

    @GetMapping("/{idUser}/notifications/storage")
    @JsonView(Views.NotificationDetail.class)
    public ResponseEntity<?> getByIsDeleted(@AuthenticationPrincipal CustomUserDetails user){
        return ResponseEntity.ok(notificationOfUserService.getByIsDeleted(user.getId()));
    }

    @GetMapping("/{idUser}/notifications")
    @JsonView(Views.NotificationDetail.class)
    public ResponseEntity<?> getByNotIsDeleted(@AuthenticationPrincipal CustomUserDetails user){
        return ResponseEntity.ok(notificationOfUserService.getByIsNotDelete(user.getId()));
    }

    @GetMapping("/{idUser}/notifications/{id}")
    @JsonView(Views.TaskDetail.class)
    public ResponseEntity<?> getDetail(@AuthenticationPrincipal CustomUserDetails user, @PathVariable Long id){
        return ResponseEntity.ok(notificationOfUserService.readNotification(id, user.getId()));
    }

    @DeleteMapping("/{idUser}/notifications/{id}")
    public ResponseEntity<?> delete(@AuthenticationPrincipal CustomUserDetails user, @PathVariable Long id){
        notificationOfUserService.delete(id, user.getId());
        return ResponseEntity.ok("Notification of user deleted successfully");
    }
}
