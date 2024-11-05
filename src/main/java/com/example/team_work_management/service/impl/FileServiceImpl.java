package com.example.team_work_management.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.example.team_work_management.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final AmazonS3 s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Override
    public String upload(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();

        InputStream inputStream = file.getInputStream();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(inputStream.available());
        if (fileName != null && fileName.endsWith(".txt")) {
            metadata.setContentType("text/plain; charset=utf-8");
        } else {
            metadata.setContentType(file.getContentType());
        }

        PutObjectRequest request = new PutObjectRequest(bucketName, fileName, inputStream, metadata);
        s3Client.putObject(request);
        return s3Client.getUrl(bucketName, fileName).toString();
    }

    @Override
    public boolean download(String fileName) throws IOException {
        S3Object s3Object = s3Client.getObject(new GetObjectRequest(bucketName, fileName));

        String userHome = System.getProperty("user.home");
        File downloadsDir = Paths.get(userHome, "Downloads").toFile();
        File outputFile = new File(downloadsDir, fileName);

        try(InputStream inputStream = s3Object.getObjectContent();
            FileOutputStream outputStream = new FileOutputStream(outputFile)){
            byte[] buffer = new byte[1024];
            int bytesRead;
            while((bytesRead = inputStream.read(buffer)) != -1){
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        return true;
    }

    @Override
    public boolean delete(String name) {
        s3Client.deleteObject(bucketName, name);
        return true;
    }
}
