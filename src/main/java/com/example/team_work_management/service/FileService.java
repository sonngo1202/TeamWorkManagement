package com.example.team_work_management.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    String upload(MultipartFile file) throws IOException;
    boolean download(String fileName) throws IOException;
    boolean delete(String name);
}
