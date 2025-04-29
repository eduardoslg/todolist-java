package br.com.eduardoslg.todolist.tasks;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.com.eduardoslg.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;




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

    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody TasksModel task, HttpServletRequest request, @PathVariable UUID id) {
        var userId = request.getAttribute("userId");

        var tasksExists = this.taskRepository.findById(id).orElse(null);

        if (tasksExists == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nenhuma task encontrada");
        }

        if (!task.getUserId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Você não tem permissão para alterar essa tarefa");
        }

        Utils.copyNonNullProperties(task, tasksExists);

        TasksModel output = this.taskRepository.save(tasksExists);
        return ResponseEntity.ok().body(output);
    }
    
    @GetMapping("/list/table")
    public ResponseEntity list(HttpServletRequest request) {
        var userId = request.getAttribute("userId");

        List<TasksModel> output =this.taskRepository.findByUserId((UUID) userId);

        return ResponseEntity.status(HttpStatus.OK).body(output);

    }
}
