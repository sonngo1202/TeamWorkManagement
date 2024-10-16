package com.example.team_work_management.service.impl;

import com.example.team_work_management.entity.User;
import com.example.team_work_management.exception.error.EmailAlreadyExistsException;
import com.example.team_work_management.exception.error.EmailNotFoundException;
import com.example.team_work_management.exception.error.InvalidVerificationCodeException;
import com.example.team_work_management.exception.error.VerificationCodeExpiredException;
import com.example.team_work_management.repository.IUserRepository;
import com.example.team_work_management.security.CustomUserDetailService;
import com.example.team_work_management.security.JwtUtil;
import com.example.team_work_management.service.AuthService;
import com.example.team_work_management.service.EmailService;
import com.example.team_work_management.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ImageService imageService;

    @Override
    public boolean register(User user) {
        Optional<User> userOptional = userRepository.findByEmail(user.getEmail());
        if(!userOptional.isEmpty()){
            User userPre = userOptional.get();
            if(userPre.isActive()){
                throw new EmailAlreadyExistsException("Email already exists!");
            }

            userPre.setFullName(user.getFullName());
            userPre.setEmail(user.getEmail());
            userPre.setJob(user.getJob());
            userPre.setLocation(user.getLocation());

            processRegistration(userPre);
            return true;
        }
        processRegistration(user);
        return true;
    }

    @Override
    public boolean retrieveCode(User user) {
        User userPre = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new EmailNotFoundException("Email not found"));
        userPre.setCode(generateVerifyCode());
        userPre.setExpiresAt(LocalDateTime.now().plusMinutes(10));

        userRepository.save(userPre);
        emailService.sendSimpleMessage(user.getEmail(), "Verify Code", "Your verification code is: " + userPre.getCode());

        return true;
    }

    @Override
    public boolean isVerificationCodeValid(User user) throws IOException {
        User userPre = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new EmailNotFoundException("Email not found"));

        if(!userPre.getExpiresAt().isAfter(LocalDateTime.now()) || userPre.isActive()){
            throw new VerificationCodeExpiredException("Verification code expired");
        }

        if(!userPre.getCode().equals(user.getCode())){
            throw new InvalidVerificationCodeException("Invalid verification code");
        }

        BufferedImage image = imageService.createImageUser(userPre.getFullName());
        String urlImage = imageService.uploadToFirebase(image);

        userPre.setPicture(urlImage);
        userPre.setActive(true);
        userRepository.save(userPre);

        return true;
    }

    @Override
    public boolean login(User user) {
        authenticate(user);

        final UserDetails userDetails = customUserDetailService.loadUserByUsername(user.getEmail());
        final User info = userRepository.findByEmailAndIsActiveTrue(user.getEmail()).get();

        user.setPicture(info.getPicture());
        user.setFullName(info.getFullName());
        user.setJob(info.getJob());
        user.setLocation(info.getLocation());
        user.setAccessToken(jwtUtil.generateToken(userDetails));
        user.setRefreshToken(jwtUtil.generateRefreshToken(userDetails));

        return true;
    }

    private void processRegistration(User user){
        user.setCode(generateVerifyCode());
        user.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
        emailService.sendSimpleMessage(user.getEmail(), "Verify Code", "Your verification code is: " + user.getCode());
    }

    private String generateVerifyCode(){
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < 6; i++){
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }

    private void authenticate(User user){
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
            );
        }catch (BadCredentialsException e){
            throw new BadCredentialsException("Incorrect email or password");
        }
    }

}
