package com.example.team_work_management.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "group")
@Setter
@Getter
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "create_at", nullable = false)
    private LocalDate createAt;

    @JsonIgnore
    @Column(name = "is_closed", nullable = false)
    private boolean isClosed = false;

    @Column(name = "des")
    private String des;

    @Column(name = "picture", nullable = false)
    private String picture;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private List<UserGroup> listUserGroup;

}
