package com.example.team_work_management.service.impl;

import com.example.team_work_management.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Async
    @Override
    public void sendSimpleMessage(String toEmail, String title, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(title);
        message.setText(body);
        emailSender.send(message);
    }

    @Async
    @Override
    public void sendSimpleMessageHtml(String toEmail, String title, String body) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject(title);
            helper.setText(body, true);

            emailSender.send(message);
            System.out.println("HTML email sent successfully to: " + toEmail);
        } catch (Exception e) {
            System.err.println("Error sending HTML email: " + e.getMessage());
        }
    }
}
