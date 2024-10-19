package com.example.team_work_management.service;

import com.example.team_work_management.entity.Group;

import java.io.IOException;

public interface GroupService {
    boolean add(Group group) throws IOException;
}
