package com.example.team_work_management.service;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface ImageService {
    BufferedImage createImage(String fullName);
    String uploadToFirebase(BufferedImage image) throws IOException;
}
