package com.java.todo.controller;

import com.java.todo.configuration.RepresentationModelAssemblerConfig;
import com.java.todo.exception.TodoNotFoundException;
import com.java.todo.model.Todo;
import com.java.todo.service.TodoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TodoController {
    private final static Logger logger = LoggerFactory.getLogger(TodoController.class);
    private TodoService todoService;
    private RepresentationModelAssemblerConfig todoModelAssembler;

    public TodoController(TodoService todoService, RepresentationModelAssemblerConfig todoModelAssembler) {
        this.todoService = todoService;
        this.todoModelAssembler = todoModelAssembler;
    }

    @GetMapping("/todo/{id}")
    public ResponseEntity<EntityModel<Todo>> get(@PathVariable("id") Integer id) {
        logger.debug("Inside get api.");
        Todo task = todoService.getTask(id);
        EntityModel<Todo> taskModel = todoModelAssembler.toModel(task);

        return new ResponseEntity<>(taskModel, HttpStatus.OK);
    }

    @GetMapping("/todos/{user}")
    public ResponseEntity<List<Todo>> getAll(@PathVariable("user") String user) {
        List<Todo> taskList = todoService.getTasks(user);

        if (taskList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        CollectionModel<EntityModel<Todo>> taskCollection = todoModelAssembler.toCollectionModel(taskList);
        return new ResponseEntity<>(taskList, HttpStatus.OK);
    }

    @GetMapping("/todos/uncomplete")
    public ResponseEntity<List<Todo>> getUncompleteTask() {
        List<Todo> taskList = todoService.getUncompletedTasks();

        int a = taskList.size();
        if (a > 0) {
            logger.debug("List has data");
        }
        else {
            logger.debug("List doesn't have data");
        }

        return new ResponseEntity<>(taskList, HttpStatus.OK);
    }

    @PostMapping("/todo")
    public ResponseEntity<Todo> addTask(@RequestBody Todo todo) {
        Todo savedTask = todoService.saveTodo(todo);

        return new ResponseEntity<>(savedTask, HttpStatus.CREATED);
    }

    @PutMapping("/todo/update")
    public ResponseEntity<Todo> updateTask(@RequestBody Todo todo) {
        try {
            Todo updatedTask = todoService.updateTodo(todo);

            return new ResponseEntity<>(updatedTask, HttpStatus.OK);
        } catch (TodoNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/todo/complete")
    public ResponseEntity<Todo> updateTaskStatus(@RequestBody Todo todo) {
        try {
            Todo completedTask = todoService.updateTaskStatus(todo.getId(), todo.getIsComplete());

            return new ResponseEntity<>(completedTask, HttpStatus.OK);
        } catch (TodoNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/todo/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Integer id) {
        try {
            todoService.delete(id);

            return ResponseEntity.noContent().build();
        } catch (TodoNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
