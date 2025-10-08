package com.example.countrycapital.controller;

import com.example.countrycapital.entity.Task;
import com.example.countrycapital.entity.TaskStatus;
import com.example.countrycapital.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TaskController.class, excludeAutoConfiguration = {
    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
    org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class
})
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Test
    void shouldShowTaskList() throws Exception {
        Task task = new Task("Test Task", "Description", LocalDate.now().plusDays(7));
        when(taskService.getAllTasks()).thenReturn(Arrays.asList(task));
        when(taskService.getTaskSummary()).thenReturn(new TaskService.TaskSummary(1, 0, 0, 0));
        when(taskService.getOverdueTasks()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/list"))
                .andExpect(model().attributeExists("tasks"))
                .andExpect(model().attributeExists("summary"));
    }

    @Test
    void shouldShowCreateForm() throws Exception {
        mockMvc.perform(get("/tasks/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/form"))
                .andExpect(model().attributeExists("task"))
                .andExpect(model().attributeExists("taskStatuses"));
    }

    @Test
    void shouldCreateTask() throws Exception {
        Task task = new Task("New Task", "Description", LocalDate.now().plusDays(7));
        when(taskService.saveTask(any(Task.class))).thenReturn(task);

        mockMvc.perform(post("/tasks")
                .param("title", "New Task")
                .param("description", "Description")
                .param("deliveryDate", LocalDate.now().plusDays(7).toString())
                .param("status", "IN_PROGRESS"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks"));
    }

    @Test
    void shouldShowEditForm() throws Exception {
        Task task = new Task("Test Task", "Description", LocalDate.now().plusDays(7));
        task.setId(1L);
        when(taskService.getTaskById(1L)).thenReturn(Optional.of(task));

        mockMvc.perform(get("/tasks/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/form"))
                .andExpect(model().attributeExists("task"));
    }

    @Test
    void shouldUpdateTaskStatus() throws Exception {
        Task task = new Task("Test Task", "Description", LocalDate.now().plusDays(7));
        task.setId(1L);
        when(taskService.updateTaskStatus(1L, TaskStatus.DELIVERED)).thenReturn(task);

        mockMvc.perform(post("/tasks/1/status")
                .param("status", "DELIVERED"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteTask() throws Exception {
        mockMvc.perform(post("/tasks/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks"));
    }

    @Test
    void shouldReturnTasksApi() throws Exception {
        Task task = new Task("Test Task", "Description", LocalDate.now().plusDays(7));
        when(taskService.getAllTasks()).thenReturn(Arrays.asList(task));

        mockMvc.perform(get("/tasks/api"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }
}