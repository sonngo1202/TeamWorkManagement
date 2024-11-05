package com.example.team_work_management.service;

import com.example.team_work_management.entity.Notification;
import com.example.team_work_management.entity.Task;
import com.example.team_work_management.entity.TaskInterest;
import com.example.team_work_management.entity.User;

import java.util.List;

public interface NotificationService {
    Notification add(Notification notification);
    void send(Task task, User sender, User recipient, String content);
    void sendGroup(Task task, User sender, List<TaskInterest> listTaskInterest, String content);
}
