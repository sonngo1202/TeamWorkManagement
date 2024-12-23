package com.example.team_work_management.repository;

import com.example.team_work_management.dto.GroupMemberStat;
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

    @Query("SELECT new com.example.team_work_management.dto.GroupMemberStat( " +
            "u.id, " +
            "u.fullName, " +
            "u.email, " +
            "u.picture, " +
            "SUM(CASE WHEN t.status.id = 1 THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN t.status.id = 2 THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN t.status.id = 3 THEN 1 ELSE 0 END)) " +
            "FROM User u " +
            "LEFT JOIN Task t ON u.id = t.assignee.id " +
            "LEFT JOIN WorkGroup wg ON wg.id = t.workGroup.id " +
            "LEFT JOIN Group g ON g.id = wg.group.id " +
            "JOIN UserGroup ug ON ug.user.id = u.id AND ug.group.id = :groupId AND ug.isActive = true " +
            "GROUP BY u")
    List<GroupMemberStat> getCountTask(@Param("groupId") Long groupId);
}
