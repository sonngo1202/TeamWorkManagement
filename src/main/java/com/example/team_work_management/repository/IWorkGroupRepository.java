package com.example.team_work_management.repository;

import com.example.team_work_management.entity.WorkGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IWorkGroupRepository extends JpaRepository<WorkGroup, Long> {
    Optional<WorkGroup> findByIdAndIsDeletedFalse(Long id);

    @Query("SELECT wg FROM WorkGroup wg WHERE wg.group.id = :groupId AND wg.isDeleted = false")
    List<WorkGroup> findActiveByGroupId(@Param("groupId") Long groupId);
}
