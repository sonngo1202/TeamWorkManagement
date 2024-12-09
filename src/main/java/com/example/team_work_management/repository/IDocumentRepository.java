package com.example.team_work_management.repository;

import com.example.team_work_management.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IDocumentRepository extends JpaRepository<Document, Long> {
    boolean existsByName(String name);

    @Query("SELECT d FROM Document d WHERE d.task.workGroup.group.id = :groupId")
    List<Document> findAllByGroupId(@Param("groupId") Long groupId);
}
