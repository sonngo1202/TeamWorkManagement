package com.example.team_work_management.entity;

import com.example.team_work_management.config.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "notification_of_user")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationOfUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({Views.NotificationDetail.class})
    private Long id;

    @ManyToOne
    @JoinColumn(name = "notification_id", nullable = false)
    @JsonView(Views.NotificationDetail.class)
    private Notification notification;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "is_read", nullable = false)
    @JsonProperty("isRead")
    @JsonView(Views.NotificationDetail.class)
    private boolean isRead;

    @Column(name = "is_deleted", nullable = false)
    @JsonIgnore
    private boolean isDeleted;
}
