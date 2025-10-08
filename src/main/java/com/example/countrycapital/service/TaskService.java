package com.example.countrycapital.service;

import com.example.countrycapital.entity.Task;
import com.example.countrycapital.entity.TaskStatus;
import com.example.countrycapital.entity.User;
import com.example.countrycapital.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    
    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private TenantService tenantService;
    
    public List<Task> getAllTasks() {
        User currentUser = tenantService.getCurrentUser();
        return taskRepository.findByUserOrderByDeliveryDateAsc(currentUser);
    }
    
    public Optional<Task> getTaskById(Long id) {
        User currentUser = tenantService.getCurrentUser();
        return taskRepository.findByIdAndUser(id, currentUser);
    }
    
    public Task saveTask(Task task) {
        User currentUser = tenantService.getCurrentUser();
        if (task.getCreatedDate() == null) {
            task.setCreatedDate(LocalDate.now());
        }
        // Set the current user as the owner and tenant ID
        task.setUser(currentUser);
        task.setTenantId(currentUser.getId().toString());
        return taskRepository.save(task);
    }
    
    public Task updateTask(Task task) {
        // Verify ownership before updating
        if (!tenantService.isCurrentUserOwner(task)) {
            throw new RuntimeException("Access denied: You can only update your own tasks");
        }
        return taskRepository.save(task);
    }
    
    public void deleteTask(Long id) {
        User currentUser = tenantService.getCurrentUser();
        Optional<Task> taskOptional = taskRepository.findByIdAndUser(id, currentUser);
        if (taskOptional.isPresent()) {
            taskRepository.deleteById(id);
        } else {
            throw new RuntimeException("Task not found or access denied");
        }
    }
    
    public List<Task> getTasksByStatus(TaskStatus status) {
        User currentUser = tenantService.getCurrentUser();
        return taskRepository.findByUserAndStatusOrderByDeliveryDateAsc(currentUser, status);
    }
    
    public List<Task> getOverdueTasks() {
        User currentUser = tenantService.getCurrentUser();
        return taskRepository.findOverdueTasksForUser(currentUser, LocalDate.now());
    }
    
    public Task updateTaskStatus(Long id, TaskStatus status) {
        User currentUser = tenantService.getCurrentUser();
        Optional<Task> taskOptional = taskRepository.findByIdAndUser(id, currentUser);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.setStatus(status);
            return taskRepository.save(task);
        }
        throw new RuntimeException("Task not found or access denied");
    }
    
    public TaskSummary getTaskSummary() {
        User currentUser = tenantService.getCurrentUser();
        long inProgress = taskRepository.countByUserAndStatus(currentUser, TaskStatus.IN_PROGRESS);
        long delivered = taskRepository.countByUserAndStatus(currentUser, TaskStatus.DELIVERED);
        long canceled = taskRepository.countByUserAndStatus(currentUser, TaskStatus.CANCELED);
        long overdue = getOverdueTasks().size();
        
        return new TaskSummary(inProgress, delivered, canceled, overdue);
    }
    
    // Additional tenant-aware methods
    public List<Task> searchTasks(String query) {
        User currentUser = tenantService.getCurrentUser();
        return taskRepository.findByUserAndTitleOrDescriptionContaining(currentUser, query);
    }
    
    public long getTotalTaskCount() {
        User currentUser = tenantService.getCurrentUser();
        return taskRepository.countByUser(currentUser);
    }
    
    public static class TaskSummary {
        private final long inProgress;
        private final long delivered;
        private final long canceled;
        private final long overdue;
        
        public TaskSummary(long inProgress, long delivered, long canceled, long overdue) {
            this.inProgress = inProgress;
            this.delivered = delivered;
            this.canceled = canceled;
            this.overdue = overdue;
        }
        
        public long getInProgress() { return inProgress; }
        public long getDelivered() { return delivered; }
        public long getCanceled() { return canceled; }
        public long getOverdue() { return overdue; }
        public long getTotal() { return inProgress + delivered + canceled; }
    }
}