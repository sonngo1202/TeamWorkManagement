package com.example.team_work_management.service;

import com.example.team_work_management.entity.Task;

public interface TaskService {
    Task getById(Long id);
    boolean add(Task task);
    boolean edit(Task task, Long id);
    boolean delete(Long id);
    boolean isTaskAssignedToUser(Long id, Long assigneeId);
    boolean updateStatus(Long id, Long statusId);
}
