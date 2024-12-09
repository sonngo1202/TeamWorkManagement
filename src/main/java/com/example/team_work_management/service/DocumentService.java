package com.example.team_work_management.service;

import com.example.team_work_management.entity.Document;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface DocumentService {
    boolean save(MultipartFile file, Long idTask) throws IOException;
    boolean downloadFile(Long id);
    boolean delete(Long id);
    List<Document> getALlByGroup(Long idG);
}
