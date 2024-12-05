package com.example.team_work_management.entity;

import com.example.team_work_management.config.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Summary.class)
    private Long id;

    @Column(name = "full_name", nullable = false)
    @JsonView(Views.Summary.class)
    private String fullName;

    @Column(name = "email", nullable = false)
    @JsonView(Views.Summary.class)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "job", nullable = false)
    @JsonView(Views.Summary.class)
    private String job;

    @Column(name = "location", nullable = false)
    @JsonView(Views.Summary.class)
    private String location;

    @Column(name = "picture")
    @JsonView(Views.Summary.class)
    private String picture;

    @JsonIgnore
    @Column(name = "is_active", nullable = false)
    private boolean isActive = false;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "code")
    private String code;

    @JsonIgnore
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @JsonView(Views.UserLogin.class)
    @Transient
    private String accessToken;

    @JsonView(Views.UserLogin.class)
    @Transient
    private String refreshToken;

    @OneToMany(mappedBy = "user")
    @JsonView(Views.UserDetailed.class)
    private List<UserGroup> roles;

    @Transient
    private boolean isInGroup;
}
