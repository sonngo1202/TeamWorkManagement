package com.example.team_work_management.repository;

import com.example.team_work_management.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ITaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByIdAndIsDeletedFalse(Long id);
    boolean existsByIdAndAssigneeId(Long id, Long assignee);
    List<Task> findByIsDeletedFalseAndAssigneeId(Long assigneeId);

    @Query("SELECT t FROM Task t " +
            "JOIN t.workGroup wg " +
            "JOIN wg.group g " +
            "JOIN g.listUserGroup ug " +
            "WHERE ug.user.id = :userId AND ug.isActive = true")
    List<Task> findAllTasksByUserId(@Param("userId") Long userId);


}
