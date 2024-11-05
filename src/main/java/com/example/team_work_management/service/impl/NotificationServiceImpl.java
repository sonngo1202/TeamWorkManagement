package com.example.team_work_management.service.impl;

import com.example.team_work_management.entity.*;
import com.example.team_work_management.repository.INotificationRepository;
import com.example.team_work_management.service.AuthService;
import com.example.team_work_management.service.NotificationOfUserService;
import com.example.team_work_management.service.NotificationService;
import com.example.team_work_management.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private INotificationRepository notificationRepository;

    @Autowired
    private NotificationOfUserService notificationOfUserService;

    @Override
    public Notification add(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public void send(Task task, User sender, User recipient, String content) {
        Notification notification = add(Notification.builder()
                .content(content)
                .createdAt(LocalDateTime.now())
                .creator(sender)
                .task(task).build());
        notificationOfUserService.save(NotificationOfUser.builder().notification(notification).user(recipient).build());
    }

    @Override
    public void sendGroup(Task task, User sender, List<TaskInterest> listTaskInterest, String content) {
        Notification notification = add(Notification.builder()
                .content(content)
                .createdAt(LocalDateTime.now())
                .creator(sender)
                .task(task).build());
        for(TaskInterest taskInterest  : listTaskInterest){
            notificationOfUserService.save(NotificationOfUser.builder().notification(notification).user(taskInterest.getUser()).build());
        }
    }
}
