package com.example.team_work_management.controller;

import com.example.team_work_management.config.Views;
import com.example.team_work_management.entity.Task;
import com.example.team_work_management.entity.User;
import com.example.team_work_management.entity.WorkGroup;
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
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PreAuthorize("@groupSecurityService.hasRoleInGroup(#idGroup, 'MANAGER', authentication.principal.id)")
    @PostMapping("/{idGroup}/work-group/{idWG}/tasks")
    public ResponseEntity<?> add(@RequestBody Task task, @PathVariable Long idGroup, @PathVariable Long idWG){
        task.setWorkGroup(WorkGroup.builder().id(idWG).build());
        taskService.add(task);
        return ResponseEntity.ok("Task added successfully");
    }

    @PreAuthorize("@groupSecurityService.hasInGroup(#idGroup, authentication.principal.id)")
    @GetMapping("/{idGroup}/work-group/{idWG}/tasks/{id}")
    @JsonView(Views.TaskDetail.class)
    public ResponseEntity<?> getDetail(@PathVariable Long id, @PathVariable Long idGroup){
        return ResponseEntity.ok(taskService.getById(id));
    }

    @PreAuthorize("@groupSecurityService.hasRoleInGroup(#idGroup, 'MANAGER', authentication.principal.id)")
    @PutMapping("/{idGroup}/work-group/{idWG}/tasks/{id}")
    public ResponseEntity<?> edit(@RequestBody Task task, @PathVariable Long idGroup, @PathVariable Long id){
        taskService.edit(task, id);
        return  ResponseEntity.ok("Task edited successfully");
    }

    @PreAuthorize("@groupSecurityService.hasRoleInGroup(#idGroup, 'MANAGER', authentication.principal.id)")
    @DeleteMapping("/{idGroup}/work-group/{idWG}/tasks/{id}")
    public ResponseEntity<?> delete(@RequestBody Task task, @PathVariable Long idGroup, @PathVariable Long id){
        taskService.delete(id);
        return ResponseEntity.ok("Task deleted successfully");
    }

    @PreAuthorize("@groupSecurityService.checkUserPermissionForTask(#id, authentication.principal.id)")
    @PutMapping("/{idGroup}/work-group/{idWG}/tasks/{id}/status/{idStatus}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @PathVariable Long idStatus){
        taskService.updateStatus(id, idStatus);
        return ResponseEntity.ok("Task updated status successfully");
    }
}
