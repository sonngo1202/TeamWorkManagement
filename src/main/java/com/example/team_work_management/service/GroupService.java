package com.example.team_work_management.service;

import com.example.team_work_management.entity.Group;

import java.io.IOException;
import java.util.List;

public interface GroupService {
    boolean add(Group group) throws IOException;
    boolean edit(Group group, Long id);
    boolean delete(Long id);
    List<Group> getAll();
    boolean addMember(Long idUser, Long id);
    boolean removeMember(Long id, Long idUser);
    Group getDetail(Long id);
}
