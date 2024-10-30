package com.example.team_work_management.service;

import com.example.team_work_management.entity.UserGroup;

public interface GroupMemberService {
    boolean addMemberToGroup(Long idUser, Long id);
    boolean removeMemberFromGroup(Long id, Long idUser);
    boolean addRoleOfMember(Long idUser, Long id, UserGroup userGroup);
}
