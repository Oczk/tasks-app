package io.ocz.controller;

import io.ocz.model.Task;
import io.ocz.model.contract.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
class TaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository repository;

    TaskController(TaskRepository repository) {
        this.repository = repository;
    }

    @GetMapping(value = "/tasks", params = {"!sort", "!page", "!size"})
    ResponseEntity<List<Task>> readAllTasks() {
        logger.info("Reading all tasks.");
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/tasks")
    ResponseEntity<List<Task>> readAllTasks(Pageable pageable) {
        logger.info("Custom pageable");
        return ResponseEntity.ok(repository.findAll(pageable).getContent());
    }

    @GetMapping("/tasks/{id}")
    ResponseEntity<Task> readTask(@PathVariable int id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/tasks")
    ResponseEntity<Task> createTask(@RequestBody @Valid Task task) {
        Task result = repository.save(task);
        URI location = URI.create("/" + result.getId());
        return ResponseEntity.created(location).body(result);
    }

    @Transactional
    @PutMapping("/tasks/{id}")
    public ResponseEntity<?> updateTask(@PathVariable("id") int id, @RequestBody @Valid Task task) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.findById(id)
                .ifPresent(t -> t.updateFrom(task));
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @PatchMapping("/tasks/{id}")
    public ResponseEntity<?> toggleTask(@PathVariable("id") int id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.findById(id)
                .ifPresent(task -> task.setDone(!task.isDone()));
        return ResponseEntity.noContent().build();
    }
}
