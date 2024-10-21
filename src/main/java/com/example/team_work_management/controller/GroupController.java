package com.example.team_work_management.controller;

import com.example.team_work_management.entity.Group;
import com.example.team_work_management.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/group")
public class GroupController {

    @Autowired
    private GroupService groupService;


    @PostMapping()
    public ResponseEntity<?> add(@RequestBody Group group) throws IOException {
        groupService.add(group);
        return ResponseEntity.status(201).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> edit(@RequestBody Group group, @PathVariable Long id){
        groupService.edit(group, id);
        return ResponseEntity.ok("Group edit successful");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        groupService.delete(id);
        return ResponseEntity.ok("Group closed successfully");
    }

    @GetMapping()
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(groupService.getAll());
    }

    @PostMapping("/{id}/member/{idUser}")
    public ResponseEntity<?> addMember(@PathVariable Long id, @PathVariable Long idUser){
        if(groupService.addMember(idUser, id)){
            return ResponseEntity.ok("Member added successfully");
        }
        return ResponseEntity.status(409).body("User is already in the group");

    }

    @DeleteMapping("/{id}/member/{idUser}")
    public ResponseEntity<?> removeMember(@PathVariable Long id, @PathVariable Long idUser){
        if(groupService.removeMember(id, idUser)){
            return ResponseEntity.ok("Member removed successfully");
        }
        return ResponseEntity.status(409).body("User is not already in the group or No permission to delete user");

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDetail(@PathVariable Long id){
        return ResponseEntity.ok(groupService.getDetail(id));
    }

}
