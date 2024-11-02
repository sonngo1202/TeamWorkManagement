package com.example.team_work_management.service;

import com.example.team_work_management.entity.Comment;

public interface CommentService {
    boolean add(Comment comment);
    boolean edit(Comment comment, Long id);
    boolean delete(Long id);
    Comment getById(Long id);
    boolean isUserOwnerOfComment(Long userId, Long id);
}
