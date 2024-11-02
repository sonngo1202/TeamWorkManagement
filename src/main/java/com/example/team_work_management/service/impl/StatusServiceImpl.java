package com.example.team_work_management.service.impl;

import com.example.team_work_management.entity.Status;
import com.example.team_work_management.exception.error.StatusNotFoundException;
import com.example.team_work_management.repository.IStatusRepository;
import com.example.team_work_management.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatusServiceImpl implements StatusService {

    @Autowired
    private IStatusRepository statusRepository;

    @Override
    public Status getById(Long id) {
        return statusRepository.findById(id)
                .orElseThrow(() -> new StatusNotFoundException("Status not found"));
    }

    @Override
    public boolean add(Status status) {
        statusRepository.save(status);
        return true;
    }
}
