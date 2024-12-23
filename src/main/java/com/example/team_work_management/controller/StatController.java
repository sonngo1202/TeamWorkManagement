package com.example.team_work_management.controller;

import com.example.team_work_management.config.Views;
import com.example.team_work_management.security.CustomUserDetails;
import com.example.team_work_management.service.StatService;
import com.example.team_work_management.service.TaskService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/groups")
public class StatController {

    @Autowired
    private StatService statService;

    @Autowired
    private TaskService taskService;

    @GetMapping("/{id}/stat/by-status")
    @PreAuthorize("@groupSecurityService.hasRoleInGroup(#id, 'MANAGER', authentication.principal.id)")
    public ResponseEntity<?> getCountTaskByStatus(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails){
        return ResponseEntity.ok(statService.getCountTaskByStatus(id));
    }

    @GetMapping("/{id}/stat/by-member")
    @PreAuthorize("@groupSecurityService.hasRoleInGroup(#id, 'MANAGER', authentication.principal.id)")
    public ResponseEntity<?> getCountTaskByUser(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails){
        return ResponseEntity.ok(statService.getCountTaskByUser(id));
    }

    @GetMapping("/{id}/stat/by-status/{idStatus}")
    @PreAuthorize("@groupSecurityService.hasRoleInGroup(#id, 'MANAGER', authentication.principal.id)")
    @JsonView(Views.NotificationDetail.class)
    public ResponseEntity<?> getByStatusAndGroup(@PathVariable Long id, @PathVariable Long idStatus, @AuthenticationPrincipal CustomUserDetails userDetails){
        return ResponseEntity.ok(taskService.getByStatusAndGroup(id, idStatus));
    }

    @GetMapping("/{id}/stat/by-member/{idUser}")
    @PreAuthorize("@groupSecurityService.hasRoleInGroup(#id, 'MANAGER', authentication.principal.id)")
    @JsonView(Views.NotificationDetail.class)
    public ResponseEntity<?> getByUserAndGroup(@PathVariable Long id, @PathVariable Long idUser, @AuthenticationPrincipal CustomUserDetails userDetails){
        return ResponseEntity.ok(taskService.getByUserAndGroup(idUser, id));
    }
}
