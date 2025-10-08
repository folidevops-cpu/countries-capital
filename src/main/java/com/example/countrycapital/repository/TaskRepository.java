package com.example.countrycapital.repository;

import com.example.countrycapital.entity.Task;
import com.example.countrycapital.entity.TaskStatus;
import com.example.countrycapital.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    // Original queries (for backward compatibility)
    List<Task> findByStatus(TaskStatus status);
    
    List<Task> findByStatusOrderByDeliveryDateAsc(TaskStatus status);
    
    List<Task> findAllByOrderByDeliveryDateAsc();
    
    List<Task> findByDeliveryDateBefore(LocalDate date);
    
    @Query("SELECT t FROM Task t WHERE t.deliveryDate < :date AND t.status != 'DELIVERED'")
    List<Task> findOverdueTasks(LocalDate date);
    
    @Query("SELECT t FROM Task t WHERE t.deliveryDate BETWEEN :startDate AND :endDate")
    List<Task> findTasksBetweenDates(LocalDate startDate, LocalDate endDate);
    
    long countByStatus(TaskStatus status);
    
    // SaaS-specific queries for user isolation
    
    // Find tasks by user (tenant isolation)
    List<Task> findByUser(User user);
    
    // Find tasks by user ordered by delivery date
    List<Task> findByUserOrderByDeliveryDateAsc(User user);
    
    // Find tasks by user and status
    List<Task> findByUserAndStatus(User user, TaskStatus status);
    
    // Find tasks by user and status ordered by delivery date
    List<Task> findByUserAndStatusOrderByDeliveryDateAsc(User user, TaskStatus status);
    
    // Find task by ID and user (for security)
    Optional<Task> findByIdAndUser(Long id, User user);
    
    // Find tasks by tenant ID
    List<Task> findByTenantId(String tenantId);
    
    // Find tasks by user and delivery date before (overdue for user)
    List<Task> findByUserAndDeliveryDateBefore(User user, LocalDate date);
    
    // Find overdue tasks for user
    @Query("SELECT t FROM Task t WHERE t.user = :user AND t.deliveryDate < :date AND t.status != 'DELIVERED'")
    List<Task> findOverdueTasksForUser(@Param("user") User user, @Param("date") LocalDate date);
    
    // Find tasks between dates for user
    @Query("SELECT t FROM Task t WHERE t.user = :user AND t.deliveryDate BETWEEN :startDate AND :endDate")
    List<Task> findTasksBetweenDatesForUser(@Param("user") User user, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    // Count tasks by user
    long countByUser(User user);
    
    // Count tasks by user and status
    long countByUserAndStatus(User user, TaskStatus status);
    
    // Custom query for user's tasks with search
    @Query("SELECT t FROM Task t WHERE t.user = :user AND " +
           "(LOWER(t.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(t.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Task> findByUserAndTitleOrDescriptionContaining(@Param("user") User user, 
                                                        @Param("search") String search);
    
    // Find recent tasks for user (last N days)
    @Query("SELECT t FROM Task t WHERE t.user = :user AND t.createdDate >= :date ORDER BY t.createdDate DESC")
    List<Task> findRecentTasksForUser(@Param("user") User user, @Param("date") LocalDate date);
}