package com.example.team_work_management.entity;

import com.example.team_work_management.config.Views;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "priority_level")
@Getter
@Setter
@Builder
public class PriorityLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({Views.Summary.class})
    private Long id;

    @Column(name = "name")
    @JsonView({Views.Summary.class})
    private String name;

    @Column(name = "level")
    @JsonView({Views.Summary.class})
    private int level;
}
