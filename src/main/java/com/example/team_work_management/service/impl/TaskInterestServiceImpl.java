package com.example.team_work_management.service.impl;

import com.example.team_work_management.entity.TaskInterest;
import com.example.team_work_management.repository.ITaskInterestRepository;
import com.example.team_work_management.service.TaskInterestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskInterestServiceImpl implements TaskInterestService {

    @Autowired
    private ITaskInterestRepository taskInterestRepository;

    @Override
    public boolean save(TaskInterest taskInterest) {
        taskInterestRepository.save(taskInterest);
        return true;
    }

    @Override
    public boolean delete(TaskInterest taskInterest) {
        taskInterestRepository.deleteByUserIdAndTaskId(taskInterest.getUser().getId(), taskInterest.getTask().getId());
        return true;
    }

    @Override
    public List<TaskInterest> getByTaskAndExcludeUser(TaskInterest taskInterest) {
        return taskInterestRepository.findAllByTaskIdAdUserIdNot(taskInterest.getTask().getId(), taskInterest.getUser().getId());
    }

    @Override
    public boolean hasInTaskInterest(Long taskId, Long userId) {
        return taskInterestRepository.existsByTaskIdAndUserId(taskId, userId);
    }
}
