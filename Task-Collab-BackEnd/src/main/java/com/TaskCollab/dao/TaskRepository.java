package com.TaskCollab.dao;

import com.TaskCollab.Entity.Task;

import lombok.NonNull;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
@Query("SELECT t FROM Task t WHERE t.assigned_To = :assignedTo")
List<Task> findByAssigned_To(@Param("assignedTo") String assignedTo);

@Query("SELECT t FROM Task t WHERE t.task_Id = :task_Id")
Optional<Task> findById(@Param("task_Id") Long task_Id);

}