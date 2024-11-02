package com.example.team_work_management.repository;

import com.example.team_work_management.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ITagRepository extends JpaRepository<Tag, Long> {
    @Transactional
    void deleteByCommentId(Long commentId);
}
