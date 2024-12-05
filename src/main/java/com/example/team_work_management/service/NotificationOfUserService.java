package com.example.team_work_management.service;

import com.example.team_work_management.entity.NotificationOfUser;
import com.example.team_work_management.entity.Task;

import java.util.List;

public interface NotificationOfUserService {
    boolean save(NotificationOfUser notificationReadStatus);
    boolean delete(Long idNotification, Long idUser);
    List<NotificationOfUser> getByIsNotDelete(Long idUser);
    List<NotificationOfUser> getByIsDeleted(Long idUser);
    boolean readNotification(Long idNotification, Long idUser);
}
