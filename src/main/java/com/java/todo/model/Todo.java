package com.java.todo.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "todo")
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "task")
    private String task;

    @Column(name = "description")
    private String description;

    @Column(name = "iscomplete")
    private boolean isComplete;

    @Column(name = "username")
    private String username;

    @Column(name = "targetdate")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date targetdate;

    public Todo() {

    }

    public Todo(String task, String description, boolean isComplete, String username, Date targetdate) {
        this.task = task;
        this.description = description;
        this.isComplete = 	isComplete;
        this.username = username;
        this.targetdate = targetdate;
    }

    public int getId() {
        return id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }

    public Date getTargetdate() {
        return targetdate;
    }
    public void setTargetdate(Date targetdate) {
        this.targetdate = targetdate;
    }
}
