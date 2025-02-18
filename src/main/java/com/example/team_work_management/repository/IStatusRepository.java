package com.example.team_work_management.repository;

import com.example.team_work_management.dto.StatusStat;
import com.example.team_work_management.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IStatusRepository extends JpaRepository<Status, Long> {

    @Query("SELECT new com.example.team_work_management.dto.StatusStat(s.id, s.name, COUNT(CASE WHEN g.id IS NOT NULL THEN t.id ELSE NULL END), " +
            "SUM(CASE WHEN g.id IS NOT NULL AND t.isDelay = true THEN 1 ELSE 0 END)) " +
            "FROM Status s " +
            "LEFT JOIN Task t ON t.status.id = s.id " +
            "LEFT JOIN WorkGroup wg ON wg.id = t.workGroup.id " +
            "LEFT JOIN Group g ON g.id = wg.group.id AND g.id = :idGroup AND t.isDeleted = false AND wg.isDeleted = false " +
            "GROUP BY s.id")
    List<StatusStat> countTask(@Param("idGroup") Long idGroup);
}
