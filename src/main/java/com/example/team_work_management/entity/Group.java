package com.example.team_work_management.entity;

import com.example.team_work_management.config.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "`group`")
@Setter
@Getter
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Summary.class)
    private Long id;

    @Column(name = "name", nullable = false)
    @JsonView(Views.Summary.class)
    private String name;

    @Column(name = "create_at", nullable = false)
    @JsonView(Views.Summary.class)
    private LocalDate createAt;

    @JsonIgnore
    @Column(name = "is_closed", nullable = false)
    private boolean isClosed = false;

    @Column(name = "des")
    @JsonView(Views.Summary.class)
    private String des;

    @Column(name = "picture", nullable = false)
    @JsonView(Views.Summary.class)
    private String picture;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    @JsonView(Views.Detailed.class)
    private List<UserGroup> listUserGroup;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    @JsonView(Views.Detailed.class)
    private List<WorkGroup> listWorkGroup;

    @PostLoad
    public void filterWorkGroup(){
        if(listWorkGroup != null){
             listWorkGroup = listWorkGroup.stream()
                    .filter(workGroup -> !workGroup.isDeleted())
                    .collect(Collectors.toList());
        }
    }

}
