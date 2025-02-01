package com.example.team_work_management.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Bean
    public void initFirebase() throws IOException{
        InputStream serviceAccount = getClass().getClassLoader()
                .getResourceAsStream("static/datn-5ae48-firebase-adminsdk-dp31c-13d03bec68.json");

        if (serviceAccount == null) {
            throw new FileNotFoundException("Firebase config file not found!");
        }

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket("datn-5ae48.appspot.com")
                .build();
        FirebaseApp.initializeApp(options);
    }
}
