package com.example.team_work_management.repository;

import com.example.team_work_management.entity.Group;
import com.example.team_work_management.entity.User;
import com.example.team_work_management.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface IUserGroupRepository extends JpaRepository<UserGroup, Long> {
    Optional<UserGroup> findByUserAndGroupAndIsActiveTrue(User user, Group group);
    List<UserGroup> findByUserAndIsActiveTrue(User user);
    List<UserGroup> findByGroupAndIsActiveTrue(Group group);
}
