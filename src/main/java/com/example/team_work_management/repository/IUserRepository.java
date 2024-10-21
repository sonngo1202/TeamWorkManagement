package com.example.team_work_management.repository;

import com.example.team_work_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndIsActiveTrue(String email);

    @Query("SELECT u FROM User u WHERE u.email LIKE %:key% AND u.id NOT IN (SELECT ug.user.id FROM UserGroup ug WHERE ug.group.id = :groupId)")
    List<User> findByEmailNotInGroup(@Param("key") String key, @Param("groupId") Long groupId);
}
