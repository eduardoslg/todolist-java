package br.com.eduardoslg.todolist.tasks;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/v1/tasks")
public class TasksController {

@Autowired
private ITasksRepository taskRepository;

    @PostMapping("")
    public ResponseEntity create(@RequestBody TasksModel task, HttpServletRequest request) {
        var userId = request.getAttribute("userId");

        TasksModel taskExists = this.taskRepository.findByTitle(task.getTitle());


        if (taskExists != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task already exists");
        }

        var currentDate = LocalDateTime.now();

        if (currentDate.isAfter(task.getStartedAt()) || currentDate.isAfter(task.getFinishedAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de início/término deve ser maior que a data atual");
        }

        if (task.getStartedAt().isAfter(task.getFinishedAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de início deve ser menor que a data de término");
        }

        task.setUserId((UUID) userId);
        TasksModel output = this.taskRepository.save(task);

        return ResponseEntity.status(HttpStatus.CREATED).body(output);
    }

    @GetMapping("/list/table")
    public List<TasksModel> list() {
        List<TasksModel> output = this.taskRepository.findAll();
        return output;
    }

}
