package com.example.team_work_management.service.impl;

import com.example.team_work_management.entity.Tag;
import com.example.team_work_management.repository.ITagRepository;
import com.example.team_work_management.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private ITagRepository tagRepository;

    @Override
    public boolean save(Tag tag) {
        tagRepository.save(tag);
        return true;
    }

    @Override
    public boolean deleteByCommentId(Long commentId) {
        tagRepository.deleteByCommentId(commentId);
        return true;
    }
}
