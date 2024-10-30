package com.example.team_work_management.service.impl;

import com.example.team_work_management.entity.Group;
import com.example.team_work_management.entity.User;
import com.example.team_work_management.entity.UserGroup;
import com.example.team_work_management.repository.IUserGroupRepository;
import com.example.team_work_management.service.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserGroupServiceImpl implements UserGroupService {

    @Autowired
    private IUserGroupRepository userGroupRepository;

    @Override
    public boolean save(UserGroup userGroup) {
        userGroupRepository.save(userGroup);
        return true;
    }

    @Override
    public UserGroup get(User user, Group group) {
        return userGroupRepository.findByUserAndGroupAndIsActiveTrue(user, group)
                .orElse(null);
    }

    @Override
    public List<UserGroup> getByUser(User user) {
        return userGroupRepository.findByUserAndIsActiveTrue(user);
    }


    @Override
    public boolean hasRoleInGroup(Long groupId, String role, Long userId) {
        return userGroupRepository.existsByGroupIdAndUserIdAndRoleAndIsActiveTrue(groupId, userId, role);
    }

    @Override
    public boolean hasInGroup(Long groupId, Long userId) {
        return userGroupRepository.existsByGroupIdAndUserIdAndIsActiveTrue(groupId, userId);
    }

}
