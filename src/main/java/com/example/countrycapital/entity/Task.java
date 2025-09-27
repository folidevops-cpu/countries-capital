package com.example.countrycapital.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "tasks")
public class Task {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Task title is required")
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @NotNull(message = "Delivery date is required")
    @Column(nullable = false)
    private LocalDate deliveryDate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status = TaskStatus.IN_PROGRESS;
    
    @Column(updatable = false)
    private LocalDate createdDate = LocalDate.now();
    
    // Constructors
    public Task() {}
    
    public Task(String title, String description, LocalDate deliveryDate) {
        this.title = title;
        this.description = description;
        this.deliveryDate = deliveryDate;
        this.status = TaskStatus.IN_PROGRESS;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }
    
    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
    
    public TaskStatus getStatus() {
        return status;
    }
    
    public void setStatus(TaskStatus status) {
        this.status = status;
    }
    
    public LocalDate getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }
    
    // Utility methods
    public boolean isOverdue() {
        return deliveryDate.isBefore(LocalDate.now()) && status != TaskStatus.DELIVERED;
    }
    
    public String getStatusBadgeClass() {
        return switch (status) {
            case IN_PROGRESS -> "badge-primary";
            case DELIVERED -> "badge-success";
            case CANCELED -> "badge-secondary";
        };
    }
    
    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", deliveryDate=" + deliveryDate +
                '}';
    }
}