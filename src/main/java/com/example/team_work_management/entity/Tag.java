package com.example.team_work_management.entity;

import com.example.team_work_management.config.Views;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tag")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start", nullable = false)
    @JsonView(Views.Summary.class)
    private int start;

    @Column(name = "end", nullable = false)
    @JsonView(Views.Summary.class)
    private int end;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonView(Views.Summary.class)
    private User user;
}
