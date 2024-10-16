package com.example.team_work_management.service.impl;

import com.example.team_work_management.service.ImageService;
import com.google.cloud.storage.BlobInfo;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {
    @Override
    public BufferedImage createImageUser(String fullName) {
        String initials = getInitials(fullName);

        //Size image
        int width = 200;
        int height = 200;

        //Create image background white
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        //Set background white
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        //Set font text and color text
        g2d.setFont(new Font("Arial", Font.BOLD, 80));
        g2d.setColor(generateTextColor());

        //Get size text
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(initials);
        int textHeight = fm.getAscent();

        int x = (width - textWidth) / 2;
        int y = (height - textHeight) / 2 + 65;
        g2d.drawString(initials, x, y);

        g2d.dispose();
        return image;
    }

    @Override
    public String uploadToFirebase(BufferedImage image) throws IOException {
        //Cover image to byte array
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, "png", os);
        byte[] bytes = os.toByteArray();

        //Create object InputStream from byte array
        InputStream is = new ByteArrayInputStream(bytes);

        String fileName = UUID.randomUUID().toString() + ".png";

        BlobInfo blobInfo = BlobInfo.newBuilder(StorageClient.getInstance().bucket().getName(), fileName)
                .setContentType("image/png")
                .build();
        StorageClient.getInstance().bucket().create(fileName, is, blobInfo.getContentType());

        return fileName;
    }

    private String getInitials(String fullName){
        String[] names = fullName.split("\\s+");
        return names[0].substring(0,1).toUpperCase() + names[names.length-1].substring(0,1).toUpperCase();
    }

    private Color generateTextColor(){
        Random random = new Random();
        int r, g, b;
        do {
            r = random.nextInt(201);
            g = random.nextInt(201);
            b = random.nextInt(201);
        } while (r > 200 && g > 200 && b > 200);

        return new Color(r, g, b);
    }
}
