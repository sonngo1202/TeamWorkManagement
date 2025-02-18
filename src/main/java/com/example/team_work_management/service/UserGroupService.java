package com.example.team_work_management.service;

import com.example.team_work_management.entity.Group;
import com.example.team_work_management.entity.User;
import com.example.team_work_management.entity.UserGroup;

import java.util.List;

public interface UserGroupService {
    boolean save(UserGroup userGroup);
    UserGroup get(User user, Group group);
    List<UserGroup> getByUser(User user);
    boolean hasRoleInGroup(Long groupId, String role, Long userId);
    boolean hasInGroup(Long groupId, Long userId);
}
