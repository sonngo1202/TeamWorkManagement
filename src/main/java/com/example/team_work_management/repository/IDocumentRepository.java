package com.example.team_work_management.repository;

import com.example.team_work_management.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDocumentRepository extends JpaRepository<Document, Long> {
    boolean existsByName(String name);
}
