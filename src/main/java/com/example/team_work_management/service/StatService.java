package com.example.team_work_management.service;

import com.example.team_work_management.dto.GroupMemberStat;
import com.example.team_work_management.dto.StatusStat;

import java.util.List;

public interface StatService {
    List<StatusStat> getCountTaskByStatus(Long idGroup);
    List<GroupMemberStat> getCountTaskByUser(Long idGroup);
}
