package com.example.team_work_management.entity;

import com.example.team_work_management.config.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "comment")
@Getter
@Setter
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false)
    @JsonView(Views.Summary.class)
    private String content;

    @Column(name = "update_at", nullable = false)
    @JsonView(Views.Summary.class)
    private LocalDateTime updateAt;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    @JsonView(Views.Summary.class)
    private User creator;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    @JsonIgnore
    private Task task;

    @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY)
    @JsonView(Views.Summary.class)
    private List<Tag> listTag;
}
