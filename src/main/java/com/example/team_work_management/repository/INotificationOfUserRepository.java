package com.example.team_work_management.repository;

import com.example.team_work_management.entity.NotificationOfUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface INotificationOfUserRepository extends JpaRepository<NotificationOfUser, Long> {
    Optional<NotificationOfUser> findByNotificationIdAndUserId(Long notificationId, Long userId);
    List<NotificationOfUser> findByUserIdAndIsDeletedTrue(Long userId);
    List<NotificationOfUser> findByUserIdAndIsDeletedFalse(Long userId);
}
