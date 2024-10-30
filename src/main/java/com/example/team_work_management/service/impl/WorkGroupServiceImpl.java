package com.example.team_work_management.service.impl;

import com.example.team_work_management.entity.Group;
import com.example.team_work_management.entity.WorkGroup;
import com.example.team_work_management.exception.error.WorkGroupNotFoundException;
import com.example.team_work_management.repository.IWorkGroupRepository;
import com.example.team_work_management.service.AuthService;
import com.example.team_work_management.service.GroupService;
import com.example.team_work_management.service.WorkGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkGroupServiceImpl implements WorkGroupService {

    @Autowired
    private IWorkGroupRepository workGroupRepository;

    @Autowired
    private GroupService groupService;

    @Autowired
    private AuthService authService;

    @Override
    public boolean save(WorkGroup workGroup) {
        Group group = groupService.getById(workGroup.getGroup().getId());

        workGroup.setGroup(group);
        workGroup.setDeleted(false);
        workGroupRepository.save(workGroup);
        return true;
    }

    @Override
    public boolean save(WorkGroup workGroup, Long id) {
        WorkGroup workGroupEdit = getById(id);
        workGroupEdit.setName(workGroup.getName());
        workGroupRepository.save(workGroupEdit);
        return true;
    }

    @Override
    public boolean delete(Long id) {
        WorkGroup workGroup = getById(id);
        workGroup.setDeleted(true);
        workGroupRepository.save(workGroup);
        return true;
    }

    @Override
    public WorkGroup getById(Long id) {
        return workGroupRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new WorkGroupNotFoundException("Work Group not found"));
    }

    @Override
    public List<WorkGroup> getByGroupId(Long idGroup) {
        return workGroupRepository.findActiveByGroupId(idGroup);
    }
}
