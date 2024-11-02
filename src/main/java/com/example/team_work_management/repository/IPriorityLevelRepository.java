package com.example.team_work_management.repository;

import com.example.team_work_management.entity.PriorityLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPriorityLevelRepository extends JpaRepository<PriorityLevel, Long> {
}
