package com.example.team_work_management.service.impl;

import com.example.team_work_management.dto.GroupMemberStat;
import com.example.team_work_management.dto.StatusStat;
import com.example.team_work_management.repository.IStatusRepository;
import com.example.team_work_management.repository.IUserRepository;
import com.example.team_work_management.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatServiceImpl implements StatService {

    @Autowired
    private IStatusRepository statusRepository;

    @Autowired
    private IUserRepository userRepository;

    @Override
    public List<StatusStat> getCountTaskByStatus(Long idGroup) {
        return statusRepository.countTask(idGroup);
    }

    @Override
    public List<GroupMemberStat> getCountTaskByUser(Long idGroup) {
        return userRepository.getCountTask(idGroup);
    }
}
