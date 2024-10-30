package com.example.team_work_management.service.impl;

import com.example.team_work_management.entity.Group;
import com.example.team_work_management.entity.User;
import com.example.team_work_management.entity.UserGroup;
import com.example.team_work_management.exception.error.AccessDeniedException;
import com.example.team_work_management.service.AuthService;
import com.example.team_work_management.service.GroupMemberService;
import com.example.team_work_management.service.GroupService;
import com.example.team_work_management.service.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class GroupMemberServiceImpl implements GroupMemberService {

    private final String role_Manager = "MANAGER";

    @Autowired
    private AuthService authService;

    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private GroupService groupService;

    @Override
    @Transactional
    public boolean addMemberToGroup(Long idUser, Long id) {
        Group group = groupService.getById(id);
        User member = authService.getDetail(idUser);

        if(userGroupService.get(member, group) != null){
            return false;
        }

        UserGroup userGroup = UserGroup.builder()
                .user(member)
                .group(group)
                .role("MEMBER")
                .joinedAt(LocalDateTime.now())
                .isActive(true)
                .build();

        userGroupService.save(userGroup);

        return true;
    }

    @Override
    @Transactional
    public boolean removeMemberFromGroup(Long id, Long idUser) {
        Group group = groupService.getById(id);

        User member = authService.getDetail(idUser);
        UserGroup userGroup = userGroupService.get(member, group);

        if(userGroup == null || userGroup.getRole().equalsIgnoreCase(role_Manager)){
            return false;
        }

        userGroup.setActive(false);
        userGroup.setRemovedAt(LocalDateTime.now());
        userGroupService.save(userGroup);

        return true;
    }

    @Override
    public boolean addRoleOfMember(Long idUser, Long id, UserGroup userGroupData) {
        Group group = groupService.getById(id);

        User member = authService.getDetail(idUser);
        UserGroup userGroup = userGroupService.get(member, group);

        if(userGroup == null){
            return false;
        }

        userGroup.setPosition(userGroupData.getPosition());
        userGroupService.save(userGroup);
        return true;
    }
}
