package com.example.team_work_management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StatusStat {
    private Long id;
    private String name;
    private long count;
    private long countDelay;
}
