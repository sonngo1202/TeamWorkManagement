package com.example.team_work_management.controller;

import com.example.team_work_management.entity.Group;
import com.example.team_work_management.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/group")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody Group group) throws IOException {
        groupService.add(group);
        return ResponseEntity.status(201).build();
    }
}
