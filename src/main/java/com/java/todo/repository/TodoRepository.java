package com.java.todo.repository;

import com.java.todo.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Integer> {
    @Query("SELECT t FROM Todo t WHERE t.username = ?1")
    List<Todo> findAllByUser(String user);

    @Query("UPDATE Todo t SET t.task = ?2, t.description = ?3, t.isComplete = ?4 WHERE t.id = ?1")
    @Modifying
    @Transactional
    void updateTodoById(Integer id, String task, String description, boolean isComplete);

    @Query("UPDATE Todo t SET t.isComplete = ?2 WHERE t.id = ?1")
    @Modifying
    @Transactional
    void markTaskAsCompleteById(Integer id, boolean isComplete);

    @Query("SELECT t FROM Todo t WHERE t.isComplete = false and t.targetdate < ?1")
    List<Todo> findUncompletedTasks(Date targetdate);
}