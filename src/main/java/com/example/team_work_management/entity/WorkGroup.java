package com.example.team_work_management.entity;

import com.example.team_work_management.config.Views;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "work_group")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class WorkGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Detailed.class)
    private Long id;

    @Column(name = "name")
    @JsonView(Views.Detailed.class)
    private String name;

    @Column(name = "is_deleted")
    @JsonIgnore
    private boolean isDeleted;

    @OneToMany(mappedBy = "workGroup", fetch = FetchType.LAZY)
    @JsonView(Views.Detailed.class)
    private List<Task> listTask;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    @JsonView(Views.NotificationDetail.class)
    private Group group;

    @PostLoad
    public void filterTask(){
        if(listTask != null){
            listTask = listTask.stream()
                    .filter(task -> task.getParentTask() == null)
                    .collect(Collectors.toList());
        }
    }
}
