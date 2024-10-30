package com.example.team_work_management.entity;

import com.example.team_work_management.config.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_group")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Summary.class)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonView(Views.Detailed.class)
    private User user;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    @JsonView(Views.UserLogin.class)
    private Group group;

    @Column(name = "role", nullable = false)
    @JsonView(Views.Summary.class)
    private String role;

    @Column(name = "joined_at", nullable = false)
    @JsonView(Views.Summary.class)
    private LocalDateTime joinedAt;

    @Column(name = "removed_at")
    @JsonView(Views.Summary.class)
    private LocalDateTime removedAt;

    @Column(name = "position")
    @JsonView(Views.Summary.class)
    private String position;

    @JsonIgnore
    @Column(name = "is_active", nullable = false)
    private boolean isActive;
}
