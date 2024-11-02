package com.example.team_work_management.service;

import com.example.team_work_management.entity.Notification;

public interface NotificationService {
    Notification add(Notification notification);
    boolean delete(Long id, Long userId);
}
