package br.com.eduardoslg.todolist.tasks;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "tasks")
public class TasksModel {
    
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    private UUID userId;

    @Column(length = 50, nullable = false)
    private String title;
    private String description;
    private String priority;

    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

}
