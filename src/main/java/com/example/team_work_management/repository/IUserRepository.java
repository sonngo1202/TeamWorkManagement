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

    @Query("SELECT u, CASE WHEN COUNT(ug) > 0 THEN true ELSE false END AS isInGroup " +
            "FROM User u LEFT JOIN UserGroup ug ON u.id = ug.user.id AND ug.group.id = :groupId AND ug.isActive = true " +
            "WHERE u.email LIKE CONCAT(:key, '%') AND u.isActive = true GROUP BY u.id")
    List<Object[]> findWithGroupStatus(@Param("key") String key, @Param("groupId") Long groupId);
}
