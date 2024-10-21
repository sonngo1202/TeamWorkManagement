package com.example.team_work_management.service.impl;

import com.example.team_work_management.entity.Group;
import com.example.team_work_management.entity.User;
import com.example.team_work_management.entity.UserGroup;
import com.example.team_work_management.exception.error.AccessDeniedException;
import com.example.team_work_management.exception.error.GroupNotFoundException;
import com.example.team_work_management.repository.IGroupRepository;
import com.example.team_work_management.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GroupServiceImpl implements GroupService {

    private final String role_Manager = "MANAGER";

    @Autowired
    private IGroupRepository groupRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private EmailService emailService;

    @Override
    @Transactional
    public boolean add(Group group) throws IOException {
        User manager = authService.getCurrentAuthenticatedUser();

        BufferedImage image = imageService.createImageGroup();
        String urlImage = imageService.uploadToFirebase(image);

        group.setPicture(urlImage);
        group.setCreateAt(LocalDate.now());

        Group saveGroup = groupRepository.save(group);

        UserGroup userGroup = UserGroup.builder()
                .user(manager)
                .group(saveGroup)
                .role(role_Manager)
                .joinedAt(LocalDateTime.now())
                .isActive(true)
                .build();

        userGroupService.save(userGroup);

        return true;
    }

    @Override
    @Transactional
    public boolean edit(Group group, Long id) {
        User user = authService.getCurrentAuthenticatedUser();

        Group updateGroup = groupRepository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException("Group not found"));

        if(!authService.isManagerOfGroup(user, updateGroup)){
            throw new AccessDeniedException("You do not have permission to edit this group");
        }

        updateGroup.setName(group.getName());
        updateGroup.setDes(group.getDes());
        groupRepository.save(updateGroup);
        return true;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        User user = authService.getCurrentAuthenticatedUser();

        Group existingGroup = groupRepository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException("Group not found"));

        if(!authService.isManagerOfGroup(user, existingGroup)){
            throw new AccessDeniedException("You do not have permission to edit this group");
        }

        existingGroup.setClosed(true);
        groupRepository.save(existingGroup);
        return true;
    }

    @Override
    @Transactional
    public List<Group> getAll() {

        User user = authService.getCurrentAuthenticatedUser();

        List<UserGroup> userGroups = userGroupService.getByUser(user);
        return userGroups.stream()
                .map(userGroup -> {
                    Group group = userGroup.getGroup();
                    group.setListUserGroup(null);
                    return group;
                }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean addMember(Long idUser, Long id) {
        User user = authService.getCurrentAuthenticatedUser();

        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException("Group not found"));

        if(!authService.isManagerOfGroup(user, group)){
            throw new AccessDeniedException("You do not have permission to edit this group");
        }

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
    public boolean removeMember(Long id, Long idUser) {
        User user = authService.getCurrentAuthenticatedUser();

        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException("Group not found"));

        if(!authService.isManagerOfGroup(user, group)){
            throw new AccessDeniedException("You do not have permission to edit this group");
        }

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
    @Transactional
    public Group getDetail(Long id) {

        User user = authService.getCurrentAuthenticatedUser();
        Group group = groupRepository.findByIdWithUserGroups(id)
                .orElseThrow(() -> new GroupNotFoundException("Group not found"));

        if(userGroupService.get(user, group) == null){
            throw new GroupNotFoundException("Group not found");
        }

        return group;
    }
}
