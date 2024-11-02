package com.example.team_work_management.service;

import com.example.team_work_management.entity.PriorityLevel;

public interface PriorityLevelService {
    PriorityLevel getById(Long id);
    boolean add(PriorityLevel priorityLevel);
}
