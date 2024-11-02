package com.example.team_work_management.repository;

import com.example.team_work_management.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICommentRepository extends JpaRepository<Comment, Long> {
    boolean existsByIdAndCreatorId(Long id, Long creatorId);
}
