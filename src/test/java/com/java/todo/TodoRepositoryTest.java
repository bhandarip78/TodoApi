package com.java.todo;

import com.java.todo.model.Todo;
import com.java.todo.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class TodoRepositoryTest {
    @Autowired
    private TodoRepository todoRepository;

    @Test
    public void testAddTask(){
        Todo todo = new Todo("Test task 1", "Task 1 description", false, "Mark", Date.valueOf(LocalDate.of(2023, 02, 10)));
        Todo savedTodo = todoRepository.save(todo);

        assertThat(savedTodo).isNotNull();
        assertThat(savedTodo.getId()).isGreaterThan(0);
    }

    @Test
    public void testFindUncompletedTasks() {
        Todo todo = new Todo("Test task 1", "Task 1 description", false, "Mark", Date.valueOf(LocalDate.of(2023, 02, 03)));
        Todo savedTodo = todoRepository.save(todo);

        List<Todo> selectUncompleteTask = todoRepository.findUncompletedTasks(Date.valueOf(LocalDate.now()));

        assertThat(selectUncompleteTask).isNotNull();
        assertThat(selectUncompleteTask.size()).isGreaterThan(0);
    }
}
