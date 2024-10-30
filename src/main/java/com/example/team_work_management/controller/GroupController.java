package com.example.team_work_management.controller;

import com.example.team_work_management.config.Views;
import com.example.team_work_management.entity.Group;
import com.example.team_work_management.security.CustomUserDetails;
import com.example.team_work_management.service.GroupService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping
    public ResponseEntity<?> add(@RequestBody Group group) throws IOException {
        groupService.add(group);
        return ResponseEntity.status(201).build();
    }

    @PreAuthorize("@groupSecurityService.hasRoleInGroup(#id, 'MANAGER', authentication.principal.id)")
    @PutMapping("/{id}")
    public ResponseEntity<?> edit(@RequestBody Group group, @PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails){
        groupService.edit(group, id);
        return ResponseEntity.ok("Group edit successful");
    }

    @PreAuthorize("@groupSecurityService.hasRoleInGroup(#id, 'MANAGER', authentication.principal.id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        groupService.delete(id);
        return ResponseEntity.ok("Group closed successfully");
    }

    @GetMapping
    @JsonView(Views.Summary.class)
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(groupService.getAll());
    }

    @PreAuthorize("@groupSecurityService.hasInGroup(#id, authentication.principal.id)")
    @GetMapping("/{id}")
    @JsonView(Views.Detailed.class)
    public ResponseEntity<?> getDetail(@PathVariable Long id){
        return ResponseEntity.ok(groupService.getDetail(id));
    }

}
