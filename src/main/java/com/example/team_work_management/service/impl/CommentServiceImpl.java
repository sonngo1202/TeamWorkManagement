package com.example.team_work_management.service.impl;

import com.example.team_work_management.entity.*;
import com.example.team_work_management.exception.error.CommentNotFoundException;
import com.example.team_work_management.repository.ICommentRepository;
import com.example.team_work_management.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private ICommentRepository commentRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TagService tagService;

    @Autowired
    private TaskInterestService taskInterestService;

    @Autowired
    private NotificationService notificationService;

    @Transactional
    @Override
    public boolean add(Comment comment) {
        comment.setUpdateAt(LocalDateTime.now());
        User creator = authService.getCurrentAuthenticatedUser();
        comment.setCreator(creator);
        Task task = taskService.getById(comment.getTask().getId());
        comment.setTask(task);
        Comment saveComment = commentRepository.save(comment);

        if(comment.getListTag() != null){
            for(Tag tag : comment.getListTag()){
                User tagUser = authService.getDetail(tag.getUser().getId());
                tagService.save(Tag.builder()
                        .comment(saveComment)
                        .position(tag.getPosition())
                        .user(tagUser)
                        .build());

                notificationService.send(task, creator, tagUser, "đã nhắc đến bạn");
            }
        }

        if(!taskInterestService.hasInTaskInterest(task.getId(), creator.getId())){
            taskInterestService.save(TaskInterest.builder()
                            .task(task)
                            .user(creator)
                            .build());
        }
        notificationService.sendGroup(task, creator, taskInterestService.getByTaskAndExcludeUser(TaskInterest.builder().task(task).user(creator).build()), "đã thêm bình luận mới");

        return true;
    }

    @Transactional
    @Override
    public boolean edit(Comment comment, Long id) {
        Comment editComment = getById(id);
        tagService.deleteByCommentId(id);
        editComment.setUpdateAt(LocalDateTime.now());
        editComment.setContent(comment.getContent());
        commentRepository.save(editComment);

        if(comment.getListTag() != null){
            for(Tag tag : comment.getListTag()){
                User tagUser = authService.getDetail(tag.getUser().getId());
                tagService.save(Tag.builder()
                        .comment(editComment)
                        .position(tag.getPosition())
                        .user(tagUser)
                        .build());

                notificationService.send(editComment.getTask(), editComment.getCreator(), tagUser, "đã nhắc đến bạn");
            }
        }

        notificationService.sendGroup(editComment.getTask(), editComment.getCreator(), taskInterestService.getByTaskAndExcludeUser(TaskInterest.builder().task(editComment.getTask()).user(editComment.getCreator()).build()), "đã thêm bình luận mới");
        return true;
    }

    @Transactional
    @Override
    public boolean delete(Long id) {
        tagService.deleteByCommentId(id);
        commentRepository.deleteById(id);
        return true;
    }

    @Override
    public Comment getById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found"));
    }

    @Override
    public boolean isUserOwnerOfComment(Long userId, Long id) {
        return commentRepository.existsByIdAndCreatorId(id, userId);
    }
}
