package com.example.team_work_management.service.impl;

import com.example.team_work_management.entity.Group;
import com.example.team_work_management.entity.User;
import com.example.team_work_management.entity.UserGroup;
import com.example.team_work_management.repository.IGroupRepository;
import com.example.team_work_management.service.AuthService;
import com.example.team_work_management.service.GroupService;
import com.example.team_work_management.service.ImageService;
import com.example.team_work_management.service.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private IGroupRepository groupRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private ImageService imageService;

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
                .role("MANAGER")
                .joinedAt(LocalDateTime.now())
                .build();

        userGroupService.save(userGroup);

        return true;
    }
}
