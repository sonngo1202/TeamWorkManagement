package com.example.team_work_management.entity;

import com.example.team_work_management.config.Views;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "document", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Summary.class)
    private Long id;

    @Column(name = "name", nullable = false)
    @JsonView(Views.Summary.class)
    private String name;

    @Column(name = "url", nullable = false)
    @JsonView(Views.Summary.class)
    private String url;

    @Column(name = "upload_at", nullable = false)
    @JsonView(Views.Summary.class)
    private LocalDateTime uploadAt;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Task task;
}
