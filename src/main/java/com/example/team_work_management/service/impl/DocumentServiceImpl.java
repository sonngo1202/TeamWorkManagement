package com.example.team_work_management.service.impl;

import com.example.team_work_management.entity.Document;
import com.example.team_work_management.entity.Task;
import com.example.team_work_management.entity.TaskInterest;
import com.example.team_work_management.entity.User;
import com.example.team_work_management.exception.error.DocumentNotFoundException;
import com.example.team_work_management.repository.IDocumentRepository;
import com.example.team_work_management.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final String content_add_document = "đã thêm một tài liệu";

    @Autowired
    private FileService fileService;

    @Autowired
    private IDocumentRepository documentRepository;

    @Autowired
    private TaskService taskService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private TaskInterestService taskInterestService;

    @Autowired
    private AuthService authService;

    @Override
    public boolean save(MultipartFile file, Long idTask) throws IOException {
        String name = file.getOriginalFilename();
        if(documentRepository.existsByName(name)){
            throw new FileAlreadyExistsException("Filename already exists");
        }
        Task task = taskService.getById(idTask);
        documentRepository.save(Document.builder()
                .name(name)
                .url(fileService.upload(file))
                .task(task)
                .uploadAt(LocalDateTime.now())
                .build());

        User assignee = authService.getCurrentAuthenticatedUser();
        notificationService.sendGroup(task, assignee, taskInterestService.getByTaskAndExcludeUser(TaskInterest.builder().task(task).user(assignee).build()), content_add_document);

        return true;
    }

    @Override
    public boolean downloadFile(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException("Document not found"));
        return true;
    }

    @Override
    public boolean delete(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException("Document not found"));
        fileService.delete(document.getName());
        documentRepository.delete(document);
        return true;
    }

    @Override
    public List<Document> getALl() {
        return documentRepository.findAll();
    }
}
