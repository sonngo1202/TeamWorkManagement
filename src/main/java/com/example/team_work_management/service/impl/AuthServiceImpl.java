package com.example.team_work_management.service.impl;

import com.example.team_work_management.dto.PasswordChangeRequest;
import com.example.team_work_management.entity.Group;
import com.example.team_work_management.entity.User;
import com.example.team_work_management.entity.UserGroup;
import com.example.team_work_management.exception.error.*;
import com.example.team_work_management.repository.IUserRepository;
import com.example.team_work_management.security.CustomUserDetailService;
import com.example.team_work_management.security.JwtUtil;
import com.example.team_work_management.service.AuthService;
import com.example.team_work_management.service.EmailService;
import com.example.team_work_management.service.ImageService;
import com.example.team_work_management.service.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private final String titleVC = "Verify Code";
    private final String bodyVC = "Your verification code is: ";
    private final String titlePRC = "Password Reset Code";
    private final String bodyPRC = "Your password reset code is: ";
    private final String role_Manager = "MANAGER";

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
    private UserGroupService userGroupService;

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
    public void retrieveCode(User user) {
        User userPre = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new UserNotFoundException("Email is incorrect"));
        userPre.setCode(generateVerifyCode());
        userPre.setExpiresAt(LocalDateTime.now().plusMinutes(10));

        userRepository.save(userPre);
        emailService.sendSimpleMessage(user.getEmail(), titleVC, bodyVC + userPre.getCode());
    }

    @Override
    public boolean isVerificationCodeValid(User user) throws IOException {
        User userPre = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new UserNotFoundException("Email is incorrect"));

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
        List<UserGroup> activeRoles = info.getRoles().stream()
                .filter(UserGroup::isActive)
                .collect(Collectors.toList());
        user.setRoles(activeRoles);

        return true;
    }

    @Override
    public boolean changePassword(PasswordChangeRequest passwordChangeRequest) {
        User user = getCurrentAuthenticatedUser();

        if(!passwordEncoder.matches(passwordChangeRequest.getCurrentPassword(), user.getPassword())){
            throw new IllegalArgumentException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(passwordChangeRequest.getNewPassword()));
        userRepository.save(user);

        return true;
    }

    @Override
    public void generatePasswordResetCode(User user) {
        User userRP = userRepository.findByEmailAndIsActiveTrue(user.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        userRP.setCode(generateVerifyCode());
        userRP.setExpiresAt(LocalDateTime.now().plusMinutes(10));

        userRepository.save(userRP);
        emailService.sendSimpleMessage(user.getEmail(), titlePRC, bodyPRC + userRP.getCode());
    }

    @Override
    public boolean restPassword(User user) {
        User userPR = userRepository.findByEmailAndIsActiveTrue(user.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if(!userPR.getExpiresAt().isAfter(LocalDateTime.now())){
            throw new VerificationCodeExpiredException("Password reset code expired");
        }

        if(!userPR.getCode().equals(user.getCode())){
            throw new InvalidVerificationCodeException("Invalid password reset code");
        }

        userPR.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(userPR);

        return true;
    }

    @Override
    public boolean updateUser(User user) {
        User userUpdate = getCurrentAuthenticatedUser();

        userUpdate.setFullName(user.getFullName());
        userUpdate.setJob(user.getJob());
        userUpdate.setLocation(user.getLocation());

        userRepository.save(userUpdate);

        return true;
    }

    @Override
    public User getCurrentAuthenticatedUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmailAndIsActiveTrue(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public User getDetail(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public List<User> searchByEmailAndGroup(String key, Long groupId) {
        List<Object[]> results = userRepository.findWithGroupStatus(key, groupId);
        List<User> users = new ArrayList<>();

        for (Object[] result : results) {
            User user = (User) result[0];
            boolean isInGroup = (Boolean) result[1];
            user.setInGroup(isInGroup);
            users.add(user);
        }
        return users;
    }

    private void processRegistration(User user){
        user.setCode(generateVerifyCode());
        user.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
        emailService.sendSimpleMessage(user.getEmail(), titleVC, bodyVC + user.getCode());
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
