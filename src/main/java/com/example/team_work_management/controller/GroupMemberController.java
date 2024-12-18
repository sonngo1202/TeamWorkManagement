package com.example.team_work_management.controller;

import com.example.team_work_management.entity.UserGroup;
import com.example.team_work_management.service.GroupMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/groups")
public class GroupMemberController {

    @Autowired
    private GroupMemberService groupMemberService;

    @PreAuthorize("@groupSecurityService.hasRoleInGroup(#id, 'MANAGER', authentication.principal.id)")
    @PostMapping("/{id}/member/{idUser}")
    public ResponseEntity<?> addMember(@PathVariable Long id, @PathVariable Long idUser){
        if(groupMemberService.addMemberToGroup(idUser, id)){
            return ResponseEntity.ok("Member added successfully");
        }
        return ResponseEntity.status(409).body("User is already in the group");

    }

    @PreAuthorize("@groupSecurityService.hasRoleInGroup(#id, 'MANAGER', authentication.principal.id)")
    @DeleteMapping("/{id}/member/{idUser}")
    public ResponseEntity<?> removeMember(@PathVariable Long id, @PathVariable Long idUser){
        groupMemberService.removeMemberFromGroup(id, idUser);
        return ResponseEntity.ok("Member removed successfully");

    }

    @PreAuthorize("@groupSecurityService.hasRoleInGroup(#id, 'MANAGER', authentication.principal.id)")
    @PutMapping("/{id}/member/{idUser}")
    public ResponseEntity<?> addRoleOfMember(@PathVariable Long id, @PathVariable Long idUser, @RequestBody UserGroup userGroupData){
        if(groupMemberService.addRoleOfMember(idUser, id, userGroupData)){
            return ResponseEntity.ok("Member added role successfully");
        }
        return ResponseEntity.status(409).body("User is not already in the group");
    }
}
