package com.java.todo.service;

import com.java.todo.exception.TodoNotFoundException;
import com.java.todo.model.Todo;
import com.java.todo.repository.TodoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class TodoService {
    private final static Logger logger = LoggerFactory.getLogger(TodoService.class);
    TodoRepository todoRepo;

    public TodoService(TodoRepository todoRepo) {
        this.todoRepo = todoRepo;
    }

    public List<Todo> getTasks(String user) {
        return todoRepo.findAllByUser(user);
    }

    public Todo getTask(Integer id) {
        return todoRepo.findById(id).get();
    }

    public List<Todo> getUncompletedTasks() {
        logger.debug("TodoService::getUncompletedTasks method");
        return  todoRepo.findUncompletedTasks(Date.valueOf(LocalDate.now()));
    }

    public Todo saveTodo(Todo todo) {
        return todoRepo.save(todo);
    }

    public Todo updateTodo(Todo todo) throws TodoNotFoundException {
        if (!todoRepo.existsById(todo.getId())) {
            throw new TodoNotFoundException();
        }

        todoRepo.updateTodoById(todo.getId(), todo.getTask(), todo.getDescription(), todo.getIsComplete());

        return todoRepo.findById(todo.getId()).get();
    }

    public Todo updateTaskStatus(Integer id, boolean isComplete) throws TodoNotFoundException {
        if (!todoRepo.existsById(id)) {
            throw new TodoNotFoundException();
        }

        todoRepo.markTaskAsCompleteById(id, isComplete);

        return todoRepo.findById(id).get();
    }

    public void delete(Integer id) throws TodoNotFoundException {
        if (!todoRepo.existsById(id)) {
            throw new TodoNotFoundException();
        }

        todoRepo.deleteById(id);
    }
}
