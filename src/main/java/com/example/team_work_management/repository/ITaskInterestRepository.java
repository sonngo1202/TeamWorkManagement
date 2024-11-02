package com.example.team_work_management.repository;

import com.example.team_work_management.entity.TaskInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITaskInterestRepository extends JpaRepository<TaskInterest, Long> {
    void deleteByUserIdAndTaskId(Long userId, Long taskId);

    @Query("SELECT ti FROM TaskInterest ti WHERE ti.task.id = :taskId AND ti.user.id <> :userId")
    List<TaskInterest> findAllByTaskIdAdUserIdNot(@Param("taskId") Long taskId, @Param("userId") Long userId);

    boolean existsByTaskIdAndUserId(Long taskId, Long userId);
}
