package com.example.countrycapital.controller;

import com.example.countrycapital.entity.Task;
import com.example.countrycapital.entity.TaskStatus;
import com.example.countrycapital.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    
    @Autowired
    private TaskService taskService;
    
    @GetMapping
    public String getAllTasks(Model model) {
        model.addAttribute("tasks", taskService.getAllTasks());
        model.addAttribute("summary", taskService.getTaskSummary());
        model.addAttribute("overdueTasks", taskService.getOverdueTasks());
        return "tasks/list";
    }
    
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("task", new Task());
        model.addAttribute("taskStatuses", TaskStatus.values());
        return "tasks/form";
    }
    
    @PostMapping
    public String createTask(@Valid @ModelAttribute Task task, 
                           BindingResult result, 
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "tasks/form";
        }
        
        taskService.saveTask(task);
        redirectAttributes.addFlashAttribute("successMessage", "Task created successfully!");
        return "redirect:/tasks";
    }
    
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Task> task = taskService.getTaskById(id);
        if (task.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Task not found!");
            return "redirect:/tasks";
        }
        
        model.addAttribute("task", task.get());
        model.addAttribute("taskStatuses", TaskStatus.values());
        return "tasks/form";
    }
    
    @PostMapping("/{id}")
    public String updateTask(@PathVariable Long id, 
                           @Valid @ModelAttribute Task task, 
                           BindingResult result, 
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "tasks/form";
        }
        
        task.setId(id);
        taskService.updateTask(task);
        redirectAttributes.addFlashAttribute("successMessage", "Task updated successfully!");
        return "redirect:/tasks";
    }
    
    @PostMapping("/{id}/delete")
    public String deleteTask(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            taskService.deleteTask(id);
            redirectAttributes.addFlashAttribute("successMessage", "Task deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting task!");
        }
        return "redirect:/tasks";
    }
    
    @PostMapping("/{id}/status")
    @ResponseBody
    public ResponseEntity<String> updateTaskStatus(@PathVariable Long id, @RequestParam TaskStatus status) {
        try {
            taskService.updateTaskStatus(id, status);
            return ResponseEntity.ok("Status updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating status");
        }
    }
    
    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<?> getTasksApi(@RequestParam(required = false) TaskStatus status) {
        if (status != null) {
            return ResponseEntity.ok(taskService.getTasksByStatus(status));
        }
        return ResponseEntity.ok(taskService.getAllTasks());
    }
    
    @GetMapping("/summary")
    @ResponseBody
    public ResponseEntity<TaskService.TaskSummary> getTaskSummary() {
        return ResponseEntity.ok(taskService.getTaskSummary());
    }
}