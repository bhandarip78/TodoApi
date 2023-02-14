package com.java.todo.datasetup;

import com.java.todo.model.Todo;
import com.java.todo.repository.TodoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class TodoDataSeedInitializer {
    private TodoRepository todoRepo;

    public TodoDataSeedInitializer(TodoRepository todoRepo){
        this.todoRepo = todoRepo;
    }

    @Bean
    public CommandLineRunner initDatabase() {
        return args -> {
            Date d = new Date(System.currentTimeMillis());
            Todo jt1 = new Todo("Go to training class", "Mandatory training", false, "John", Date.valueOf(LocalDate.of(2023, 02, 07)));
            Todo jt2 = new Todo("Meeting with client", "At downtown", false, "John", Date.valueOf(LocalDate.of(2023, 02, 07)));
            Todo jt3 = new Todo("Send an email to doctor", "Regarding medicine", false, "John", Date.valueOf(LocalDate.of(2023, 02, 06)));
            Todo mt1 = new Todo("Send design doc to security officer", "With encryption", false, "Mike", Date.valueOf(LocalDate.of(2023, 02, 15)));
            Todo mt2 = new Todo("Call to make an appointment", "Doctor appointment", false, "Mike", Date.valueOf(LocalDate.of(2023, 02, 15)));

            List<Todo> lt = new ArrayList<Todo>();
            lt.add(jt1);
            lt.add(jt2);
            lt.add(jt3);
            lt.add(mt1);
            lt.add(mt2);
            todoRepo.saveAll(lt);

            System.out.println("Todo data seed initialized");
        };
    }
}