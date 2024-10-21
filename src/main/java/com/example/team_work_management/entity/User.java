package com.example.team_work_management.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

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
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "email", nullable = false)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "job", nullable = false)
    private String job;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "picture")
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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Transient
    private String accessToken;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Transient
    private String refreshToken;

}
