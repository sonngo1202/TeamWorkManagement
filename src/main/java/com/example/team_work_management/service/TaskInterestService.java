package com.example.team_work_management.service;

import com.example.team_work_management.entity.TaskInterest;

import java.util.List;

public interface TaskInterestService {
    boolean save(TaskInterest taskInterest);
    boolean delete(TaskInterest taskInterest);
    List<TaskInterest> getByTaskAndExcludeUser(TaskInterest taskInterest);
    boolean hasInTaskInterest(Long taskId, Long userId);
}
