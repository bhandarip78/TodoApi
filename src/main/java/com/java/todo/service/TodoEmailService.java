package com.java.todo.service;

public interface TodoEmailService {
    void sendMessage(String to, String subject, String body);
}
