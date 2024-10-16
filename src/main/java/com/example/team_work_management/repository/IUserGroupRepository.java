package com.example.team_work_management.repository;

import com.example.team_work_management.entity.User;
import com.example.team_work_management.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUserGroupRepository extends JpaRepository<UserGroup, Long> {
    List<UserGroup> findByUser(User user);
}
