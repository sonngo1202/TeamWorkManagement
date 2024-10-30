package com.example.team_work_management.service;

import com.example.team_work_management.entity.WorkGroup;

import java.util.List;

public interface WorkGroupService {
    boolean save(WorkGroup workGroup);
    boolean save(WorkGroup workGroup, Long id);
    boolean delete(Long id);
    WorkGroup getById(Long id);
    List<WorkGroup> getByGroupId(Long idGroup);
}
