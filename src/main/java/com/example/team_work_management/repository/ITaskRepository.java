package com.example.team_work_management.repository;

import com.example.team_work_management.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ITaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByIdAndIsDeletedFalse(Long id);
    boolean existsByIdAndAssigneeId(Long id, Long assignee);
    @Query("SELECT t FROM Task t " +
            "JOIN t.workGroup wg " +
            "JOIN wg.group g " +
            "WHERE t.isDeleted = false AND t.assignee.id = :assigneeId AND g.isClosed = false")
    List<Task> findByIsDeletedFalseAndAssigneeIdAndGroupNotClosed(@Param("assigneeId") Long assigneeId);


    @Query("SELECT t FROM Task t " +
            "JOIN t.workGroup wg " +
            "JOIN wg.group g " +
            "JOIN g.listUserGroup ug " +
            "WHERE ug.user.id = :userId AND ug.isActive = true")
    List<Task> findAllTasksByUserId(@Param("userId") Long userId);

    @Query("SELECT t FROM Task t " +
            "JOIN t.workGroup wg " +
            "JOIN wg.group g " +
            "WHERE g.id = :groupId AND t.parentTask IS NULL")
    List<Task> findAllTasksByGroupId(@Param("groupId") Long groupId);

    @Query("SELECT COUNT(t) FROM Task t " +
            "JOIN t.workGroup wg " +
            "JOIN wg.group g " +
            "WHERE t.isDeleted = false AND wg.isDeleted = false AND t.assignee.id = :userId AND g.id = :groupId AND g.isClosed = false")
    Long countTasksByUserInGroup(@Param("userId") Long userId, @Param("groupId") Long groupId);

    @Query("SELECT t FROM Task t " +
            "JOIN t.status s " +
            "JOIN t.workGroup wg " +
            "JOIN wg.group g " +
            "WHERE g.id = :idGroup AND s.id = :idStatus AND t.isDeleted = false AND wg.isDeleted = false")
    List<Task> findByStatusAndGroup(@Param("idGroup")Long idGroup, @Param("idStatus") Long idStatus);

    @Query("SELECT t FROM Task t " +
            "JOIN t.workGroup wg " +
            "JOIN wg.group g " +
            "WHERE t.isDeleted = false AND wg.isDeleted = false AND t.assignee.id = :userId AND g.id = :groupId AND g.isClosed = false")
    List<Task> findByUserAndGroup(@Param("userId")Long userId, @Param("groupId")Long groupId);

    @Modifying
    @Transactional
    @Query("UPDATE Task t SET t.isDelay = true WHERE t.endDate <= :yesterday AND t.status.id != 3 AND t.isDelay = false")
    int updateIsDelayForTasks(@Param("yesterday") LocalDate yesterday);

    @Query("SELECT t FROM Task t WHERE t.endDate = :endDate")
    List<Task> findTasksByEndDate(LocalDate endDate);

    @Query("SELECT t FROM Task t WHERE t.endDate <= :yesterday AND t.status.id != 3 AND t.isDelay = false")
    List<Task> findTaskDelayed(LocalDate yesterday);
}
