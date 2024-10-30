package com.example.team_work_management.controller;

import com.example.team_work_management.entity.Group;
import com.example.team_work_management.entity.WorkGroup;
import com.example.team_work_management.service.WorkGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/groups")
public class WorkGroupController {

    @Autowired
    private WorkGroupService workGroupService;

    @PreAuthorize("@groupSecurityService.hasRoleInGroup(#idGroup, 'MANAGER', authentication.principal.id)")
    @PostMapping("/{idGroup}/work-group")
    public ResponseEntity<?> add(@RequestBody WorkGroup workGroup, @PathVariable Long idGroup){
        Group group = new Group();
        group.setId(idGroup);
        workGroup.setGroup(group);
        workGroupService.save(workGroup);
        return ResponseEntity.ok("Work Group added successfully");
    }

    @PreAuthorize("@groupSecurityService.hasRoleInGroup(#idGroup, 'MANAGER', authentication.principal.id)")
    @PutMapping("/{idGroup}/work-group/{id}")
    public ResponseEntity<?> edit(@RequestBody WorkGroup workGroup, @PathVariable Long id, @PathVariable Long idGroup){
        workGroupService.save(workGroup, id);
        return ResponseEntity.ok("Work Group edited successfully");
    }

    @PreAuthorize("@groupSecurityService.hasRoleInGroup(#idGroup, 'MANAGER', authentication.principal.id)")
    @DeleteMapping("/{idGroup}/work-group/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @PathVariable Long idGroup){
        workGroupService.delete(id);
        return ResponseEntity.ok("Work Group deleted successfully");
    }
}
