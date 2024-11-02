package com.example.team_work_management.controller;

import com.example.team_work_management.entity.Comment;
import com.example.team_work_management.entity.Task;
import com.example.team_work_management.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/groups")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PreAuthorize("@groupSecurityService.hasInGroup(#idGroup, authentication.principal.id)")
    @PostMapping("/{idGroup}/work-group/{idWG}/tasks/{idTask}/comments")
    public ResponseEntity<?> add(@RequestBody Comment comment, @PathVariable Long idGroup, @PathVariable Long idTask){
        comment.setTask(Task.builder().id(idTask).build());
        commentService.add(comment);
        return ResponseEntity.ok("Comment added successfully");
    }

    @PreAuthorize("@groupSecurityService.checkUserPermissionForComment(#id, authentication.principal.id)")
    @PutMapping("/{idGroup}/work-group/{idWG}/tasks/{idTask}/comments/{id}")
    public ResponseEntity<?> edit(@RequestBody Comment comment, @PathVariable Long id){
        commentService.edit(comment, id);
        return ResponseEntity.ok("Comment edited successfully");
    }

    @PreAuthorize("@groupSecurityService.checkUserPermissionForComment(#id, authentication.principal.id)")
    @DeleteMapping("/{idGroup}/work-group/{idWG}/tasks/{idTask}/comments/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        commentService.delete(id);
        return ResponseEntity.ok("Comment deleted successfully");
    }
}
