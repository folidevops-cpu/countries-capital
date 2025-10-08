package com.example.countrycapital.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {

    @Test
    void shouldCreateTaskWithDefaults() {
        Task task = new Task();
        
        assertThat(task.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
        assertThat(task.getCreatedDate()).isEqualTo(LocalDate.now());
    }

    @Test
    void shouldCreateTaskWithParameters() {
        String title = "Test Task";
        String description = "Test Description";
        LocalDate deliveryDate = LocalDate.now().plusDays(7);
        
        Task task = new Task(title, description, deliveryDate);
        
        assertThat(task.getTitle()).isEqualTo(title);
        assertThat(task.getDescription()).isEqualTo(description);
        assertThat(task.getDeliveryDate()).isEqualTo(deliveryDate);
        assertThat(task.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
    }

    @Test
    void shouldDetectOverdueTask() {
        Task task = new Task();
        task.setDeliveryDate(LocalDate.now().minusDays(1));
        task.setStatus(TaskStatus.IN_PROGRESS);
        
        assertThat(task.isOverdue()).isTrue();
    }

    @Test
    void shouldNotDetectOverdueForDeliveredTask() {
        Task task = new Task();
        task.setDeliveryDate(LocalDate.now().minusDays(1));
        task.setStatus(TaskStatus.DELIVERED);
        
        assertThat(task.isOverdue()).isFalse();
    }

    @Test
    void shouldReturnCorrectStatusBadgeClass() {
        Task task = new Task();
        
        task.setStatus(TaskStatus.IN_PROGRESS);
        assertThat(task.getStatusBadgeClass()).isEqualTo("badge-primary");
        
        task.setStatus(TaskStatus.DELIVERED);
        assertThat(task.getStatusBadgeClass()).isEqualTo("badge-success");
        
        task.setStatus(TaskStatus.CANCELED);
        assertThat(task.getStatusBadgeClass()).isEqualTo("badge-secondary");
    }
}