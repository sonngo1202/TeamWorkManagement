package com.example.team_work_management.service;

import com.example.team_work_management.entity.Tag;

public interface TagService {
    boolean save(Tag tag);
    boolean deleteByCommentId(Long commentId);
}
