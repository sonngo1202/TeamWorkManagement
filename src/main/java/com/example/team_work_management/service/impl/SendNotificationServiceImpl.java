package com.example.team_work_management.service.impl;

import com.example.team_work_management.entity.*;
import com.example.team_work_management.service.NotificationReadStatusService;
import com.example.team_work_management.service.NotificationService;
import com.example.team_work_management.service.SendNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SendNotificationServiceImpl implements SendNotificationService {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationReadStatusService notificationReadStatusService;

    @Override
    public void send(Task task, User sender, User recipient, String content) {
        Notification notification = notificationService.add(Notification.builder()
                .content(content)
                .createdAt(LocalDateTime.now())
                .creator(sender)
                .task(task).build());

        notificationReadStatusService.save(NotificationReadStatus.builder().notification(notification).user(recipient).build());
    }

    @Override
    public void sendGroup(Task task, User sender, List<TaskInterest> listTaskInterest, String content) {
        Notification notification = notificationService.add(Notification.builder()
                .content(content)
                .createdAt(LocalDateTime.now())
                .creator(sender)
                .task(task).build());
        for(TaskInterest taskInterest  : listTaskInterest){
            notificationReadStatusService.save(NotificationReadStatus.builder().notification(notification).user(taskInterest.getUser()).build());
        }
    }
}
