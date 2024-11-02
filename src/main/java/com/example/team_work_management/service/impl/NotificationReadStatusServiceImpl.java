package com.example.team_work_management.service.impl;

import com.example.team_work_management.entity.NotificationReadStatus;
import com.example.team_work_management.repository.INotificationReadStatusRepository;
import com.example.team_work_management.service.NotificationReadStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationReadStatusServiceImpl implements NotificationReadStatusService {

    @Autowired
    private INotificationReadStatusRepository notificationReadStatusRepository;

    @Override
    public boolean save(NotificationReadStatus notificationReadStatus) {
        notificationReadStatus.setRead(false);
        notificationReadStatus.setDeleted(false);
        notificationReadStatusRepository.save(notificationReadStatus);
        return true;
    }
}
