package com.example.team_work_management.entity;

import com.example.team_work_management.config.Views;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "comment")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Summary.class)
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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Task task;

    @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY)
    @JsonView(Views.Summary.class)
    private List<Tag> listTag;
}
