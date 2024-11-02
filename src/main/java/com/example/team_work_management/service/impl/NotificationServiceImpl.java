package com.example.team_work_management.service.impl;

import com.example.team_work_management.entity.Notification;
import com.example.team_work_management.repository.INotificationRepository;
import com.example.team_work_management.service.AuthService;
import com.example.team_work_management.service.NotificationService;
import com.example.team_work_management.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private INotificationRepository notificationRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private TaskService taskService;

    @Override
    public Notification add(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public boolean delete(Long id, Long userId) {
        return false;
    }
}
