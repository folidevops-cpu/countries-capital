package com.example.countrycapital.repository;

import com.example.countrycapital.entity.Task;
import com.example.countrycapital.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    List<Task> findByStatus(TaskStatus status);
    
    List<Task> findByStatusOrderByDeliveryDateAsc(TaskStatus status);
    
    List<Task> findAllByOrderByDeliveryDateAsc();
    
    List<Task> findByDeliveryDateBefore(LocalDate date);
    
    @Query("SELECT t FROM Task t WHERE t.deliveryDate < :date AND t.status != 'DELIVERED'")
    List<Task> findOverdueTasks(LocalDate date);
    
    @Query("SELECT t FROM Task t WHERE t.deliveryDate BETWEEN :startDate AND :endDate")
    List<Task> findTasksBetweenDates(LocalDate startDate, LocalDate endDate);
    
    long countByStatus(TaskStatus status);
}