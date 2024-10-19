package com.example.team_work_management.repository;

import com.example.team_work_management.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IGroupRepository extends JpaRepository<Group, Long> {
}
