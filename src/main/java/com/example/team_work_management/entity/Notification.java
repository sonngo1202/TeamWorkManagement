package com.example.team_work_management.entity;

import com.example.team_work_management.config.Views;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.NotificationDetail.class)
    private Long id;

    @Column(name = "content", nullable = false)
    @JsonView(Views.NotificationDetail.class)
    private String content;

    @Column(name = "create_at", nullable = false)
    @JsonView(Views.NotificationDetail.class)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    @JsonView(Views.NotificationDetail.class)
    private Task task;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    @JsonView(Views.NotificationDetail.class)
    private User creator;

}
