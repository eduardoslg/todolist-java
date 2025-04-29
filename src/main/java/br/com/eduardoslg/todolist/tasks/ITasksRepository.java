package br.com.eduardoslg.todolist.tasks;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ITasksRepository extends JpaRepository<TasksModel, UUID> {
    TasksModel findByTitle(String title);
}
