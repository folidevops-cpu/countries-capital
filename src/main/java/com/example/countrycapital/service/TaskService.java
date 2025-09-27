package com.example.countrycapital.service;

import com.example.countrycapital.entity.Task;
import com.example.countrycapital.entity.TaskStatus;
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
    
    public List<Task> getAllTasks() {
        return taskRepository.findAllByOrderByDeliveryDateAsc();
    }
    
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }
    
    public Task saveTask(Task task) {
        if (task.getCreatedDate() == null) {
            task.setCreatedDate(LocalDate.now());
        }
        return taskRepository.save(task);
    }
    
    public Task updateTask(Task task) {
        return taskRepository.save(task);
    }
    
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
    
    public List<Task> getTasksByStatus(TaskStatus status) {
        return taskRepository.findByStatusOrderByDeliveryDateAsc(status);
    }
    
    public List<Task> getOverdueTasks() {
        return taskRepository.findOverdueTasks(LocalDate.now());
    }
    
    public Task updateTaskStatus(Long id, TaskStatus status) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.setStatus(status);
            return taskRepository.save(task);
        }
        throw new RuntimeException("Task not found with id: " + id);
    }
    
    public TaskSummary getTaskSummary() {
        long inProgress = taskRepository.countByStatus(TaskStatus.IN_PROGRESS);
        long delivered = taskRepository.countByStatus(TaskStatus.DELIVERED);
        long canceled = taskRepository.countByStatus(TaskStatus.CANCELED);
        long overdue = getOverdueTasks().size();
        
        return new TaskSummary(inProgress, delivered, canceled, overdue);
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