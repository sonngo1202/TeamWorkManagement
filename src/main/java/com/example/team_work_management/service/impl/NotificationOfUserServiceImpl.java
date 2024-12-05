package com.example.team_work_management.service.impl;

import com.example.team_work_management.entity.NotificationOfUser;
import com.example.team_work_management.entity.Task;
import com.example.team_work_management.entity.User;
import com.example.team_work_management.exception.error.NotificationOfUserNotFoundException;
import com.example.team_work_management.repository.INotificationOfUserRepository;
import com.example.team_work_management.service.NotificationOfUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationOfUserServiceImpl implements NotificationOfUserService {

    @Autowired
    private INotificationOfUserRepository notificationOfUserRepository;

    @Override
    public boolean save(NotificationOfUser notificationReadStatus) {
        notificationReadStatus.setRead(false);
        notificationReadStatus.setDeleted(false);
        notificationOfUserRepository.save(notificationReadStatus);
        return true;
    }

    @Override
    public boolean delete(Long idNotification, Long idUser) {
        NotificationOfUser notificationOfUser = notificationOfUserRepository.findByNotificationIdAndUserId(idNotification, idUser)
                .orElseThrow(() -> new NotificationOfUserNotFoundException("Notification of user not found"));
        notificationOfUser.setDeleted(true);
        notificationOfUserRepository.save(notificationOfUser);
        return true;
    }

    @Override
    public List<NotificationOfUser> getByIsNotDelete(Long idUser) {
        return notificationOfUserRepository.findByUserIdAndIsDeletedFalse(idUser);
    }

    @Override
    public List<NotificationOfUser> getByIsDeleted(Long idUser) {
        return notificationOfUserRepository.findByUserIdAndIsDeletedTrue(idUser);
    }

    @Override
    public boolean readNotification(Long idNotification, Long idUser) {
        NotificationOfUser notificationOfUser = notificationOfUserRepository.findByNotificationIdAndUserId(idNotification, idUser)
                .orElseThrow(() -> new NotificationOfUserNotFoundException("Notification of user not found"));
        if(!notificationOfUser.isRead()){
            notificationOfUser.setRead(true);
            notificationOfUserRepository.save(notificationOfUser);
            return true;
        }
        return false;
    }
}
