package com.example.team_work_management.service.impl;

import com.example.team_work_management.entity.UserGroup;
import com.example.team_work_management.repository.IUserGroupRepository;
import com.example.team_work_management.service.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserGroupServiceImpl implements UserGroupService {

    @Autowired
    private IUserGroupRepository userGroupRepository;

    @Override
    public boolean save(UserGroup userGroup) {
        userGroupRepository.save(userGroup);
        return true;
    }
}
