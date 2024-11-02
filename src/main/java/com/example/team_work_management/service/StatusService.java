package com.example.team_work_management.service;


import com.example.team_work_management.entity.Status;

public interface StatusService {
    Status getById(Long id);
    boolean add(Status status);
}
