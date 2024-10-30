package com.example.team_work_management.security;

import com.example.team_work_management.exception.error.AccessDeniedException;
import com.example.team_work_management.service.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GroupSecurityService {

    @Autowired
    private UserGroupService userGroupService;

    public boolean hasRoleInGroup(Long groupId, String role, Long userId){
        if(!userGroupService.hasRoleInGroup(groupId, role, userId)){
            throw new AccessDeniedException("Access is denied");
        }
        return true;
    }

    public boolean hasInGroup(Long groupId, Long userId){
        if(!userGroupService.hasInGroup(groupId, userId)){
            throw new AccessDeniedException("Access is denied");
        }
        return true;
    }
}
