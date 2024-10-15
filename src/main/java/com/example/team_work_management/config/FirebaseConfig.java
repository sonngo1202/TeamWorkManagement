package com.example.team_work_management.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @Bean
    public void initFirebase() throws IOException{
        FileInputStream serviceAccount = new FileInputStream("src/main/resources/static/datn-5ae48-firebase-adminsdk-dp31c-13d03bec68.json");
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket("datn-5ae48.appspot.com")
                .build();
        FirebaseApp.initializeApp(options);
    }
}
