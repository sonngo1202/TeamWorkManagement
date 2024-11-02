package com.example.team_work_management.service.impl;

import com.example.team_work_management.entity.PriorityLevel;
import com.example.team_work_management.exception.error.PriorityLevelNotFoundException;
import com.example.team_work_management.repository.IPriorityLevelRepository;
import com.example.team_work_management.service.PriorityLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PriorityLevelServiceImpl implements PriorityLevelService {

    @Autowired
    private IPriorityLevelRepository priorityLevelRepository;

    @Override
    public PriorityLevel getById(Long id) {
        return priorityLevelRepository.findById(id)
                .orElseThrow(() -> new PriorityLevelNotFoundException("Priority Level not found"));
    }

    @Override
    public boolean add(PriorityLevel priorityLevel) {
        priorityLevelRepository.save(priorityLevel);
        return true;
    }
}
