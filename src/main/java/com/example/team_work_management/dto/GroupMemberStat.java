package com.example.team_work_management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GroupMemberStat {
    private Long id;
    private String fullName;
    private String email;
    private String picture;
    private long countNS;
    private long countIG;
    private long countCl;
}
