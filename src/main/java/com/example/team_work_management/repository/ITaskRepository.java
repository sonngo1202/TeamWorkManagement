package com.example.team_work_management.repository;

import com.example.team_work_management.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ITaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByIdAndIsDeletedFalse(Long id);
    boolean existsByIdAndAssigneeId(Long id, Long assignee);
}
