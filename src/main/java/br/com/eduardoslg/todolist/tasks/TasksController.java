package br.com.eduardoslg.todolist.tasks;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
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
    public ResponseEntity create(@RequestBody TasksModel task) {
        TasksModel taskExists = this.taskRepository.findByTitle(task.getTitle());


        if (taskExists != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task already exists");
        }

        TasksModel output = this.taskRepository.save(task);

        return ResponseEntity.status(HttpStatus.CREATED).body(output);
    }

    @GetMapping("/list/table")
    public List<TasksModel> list() {
        List<TasksModel> output = this.taskRepository.findAll();
        return output;
    }

}
