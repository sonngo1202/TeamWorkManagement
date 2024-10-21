package com.example.team_work_management.repository;

import com.example.team_work_management.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IGroupRepository extends JpaRepository<Group, Long> {
    @Query("SELECT g FROM Group g LEFT JOIN FETCH g.listUserGroup ug WHERE g.id = :id AND ug.isActive = true")
    Optional<Group> findByIdWithUserGroups(@Param("id") Long id);

}
