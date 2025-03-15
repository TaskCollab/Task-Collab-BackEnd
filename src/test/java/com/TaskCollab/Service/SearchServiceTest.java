package com.TaskCollab.Service;

import com.TaskCollab.Entity.Task;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SearchServiceTest {

    @InjectMocks
    private SearchService searchService;

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<Task> criteriaQuery;

    @Mock
    private Root<Task> root;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Task.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Task.class)).thenReturn(root);

        // Add this line to set up the select() method
        when(criteriaQuery.select(root)).thenReturn(criteriaQuery);

        when(entityManager.createQuery(criteriaQuery)).thenReturn(mock(jakarta.persistence.TypedQuery.class));
    }

    @Test
    void testSearchTasksWithAllFilters() {
        String taskTitle = "Test Task";
        String description = "Test Description";
        String assignedTo = "John Doe";
        String status = "In Progress";
        String deadline = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        searchService.searchTasks(taskTitle, description, assignedTo, status, deadline);

        verify(entityManager).createQuery(criteriaQuery);
    }

    @Test
    void testSearchTasksWithNoFilters() {
        searchService.searchTasks(null, null, null, null, null);
        verify(entityManager).createQuery(criteriaQuery);
    }

    @Test
    void testSearchTasksWithInvalidDeadline() {
        searchService.searchTasks("Test", "Desc", "John", "Status", "Invalid Deadline");
        verify(entityManager).createQuery(criteriaQuery);
    }
}