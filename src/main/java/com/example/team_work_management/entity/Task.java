package com.example.team_work_management.entity;

import com.example.team_work_management.config.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "task")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Summary.class)
    private Long id;

    @Column(name = "name", nullable = false)
    @JsonView(Views.Summary.class)
    private String name;

    @Column(name = "start_date", nullable = false)
    @JsonView(Views.Detailed.class)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    @JsonView(Views.Detailed.class)
    private LocalDate endDate;

    @Column(name = "completed_date")
    @JsonView(Views.Detailed.class)
    private LocalDate completedDate;

    @Column(name = "des")
    @JsonView(Views.Detailed.class)
    private String des;

    @Column(name = "is_deleted")
    @JsonIgnore
    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "priority_level_id", nullable = false)
    @JsonView(Views.Detailed.class)
    private PriorityLevel priorityLevel;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    @JsonView(Views.Detailed.class)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "assignee_id", nullable = false)
    @JsonView(Views.Detailed.class)
    private User assignee;

    @ManyToOne
    @JoinColumn(name = "parent_task_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Task parentTask;

    @OneToMany(mappedBy = "parentTask")
    @JsonView(Views.Detailed.class)
    private List<Task> listSubTask;

    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY)
    @JsonView(Views.TaskDetail.class)
    private List<Comment> listComment;

    @ManyToOne
    @JoinColumn(name = "work_group_id", nullable = false)
    @JsonView(Views.NotificationDetail.class)
    private WorkGroup workGroup;

    @Column(name = "is_delay")
    @JsonView(Views.Detailed.class)
    private boolean isDelay;
}
