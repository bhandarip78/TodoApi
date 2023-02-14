package com.java.todo.exception;

public class TodoNotFoundException extends Exception{
    public TodoNotFoundException(String errorMessage) {
        super(errorMessage);
    }

    public TodoNotFoundException(){
        super();
    }
}
