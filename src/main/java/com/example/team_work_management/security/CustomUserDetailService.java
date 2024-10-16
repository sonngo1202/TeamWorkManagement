package com.example.team_work_management.security;

import com.example.team_work_management.entity.User;
import com.example.team_work_management.entity.UserGroup;
import com.example.team_work_management.repository.IUserGroupRepository;
import com.example.team_work_management.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IUserGroupRepository userGroupRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailAndIsActiveTrue(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        List<UserGroup> listUserGroup = userGroupRepository.findByUser(user);

        List<SimpleGrantedAuthority> authorities = listUserGroup.stream()
                .map(userGroup -> new SimpleGrantedAuthority(
                        "ROLE_" + userGroup.getRole() + "_" + userGroup.getGroup().getId()))
                .collect(Collectors.toList());

        return new CustomUserDetails(user.getEmail(), user.getPassword(), authorities);
    }
}
