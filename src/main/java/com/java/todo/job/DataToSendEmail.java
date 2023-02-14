package com.java.todo.job;

import com.java.todo.model.EmailMessage;
import com.java.todo.model.Todo;
import com.java.todo.service.TodoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component("dataToSendEmail")
public class DataToSendEmail {
    private final static Logger logger = LoggerFactory.getLogger(DataToSendEmail.class);
    private TodoService todoService;

    private List<EmailMessage> messageList;

    public DataToSendEmail(TodoService todoService) {
        this.todoService = todoService;
        logger.debug("Inside DataToSendEmail Constructor");
    }

    public List<EmailMessage> getMessageList() {
        return getEmailMessages();
    }

    private List<EmailMessage> getEmailMessages() {
        logger.debug(("DataToSendEmail::getEmailMessage method"));
        List<EmailMessage> emailMessages = new ArrayList<>();

        List<Todo> uncompletedTask = todoService.getUncompletedTasks();

        if (uncompletedTask.size() > 0) {
            logger.debug("DatatoSendEmail::uncompleted task found");
        }
        else {
            logger.debug("DataToSendEmail::uncompleted task Notfound");
        }

        uncompletedTask.sort(Comparator.comparing(Todo::getUsername));

        String tempUserName = null;
        String tempEmailBody = "";
        for (Todo task : uncompletedTask) {
            if (tempUserName == null) {
                tempUserName = task.getUsername();
            }

            if (tempUserName != task.getUsername()) {
                EmailMessage em = new EmailMessage();
                em.setEmail("paras.bhandari@idreamsoftek.com");
                em.setBody(tempEmailBody);
                em.setSubject("Your uncompleted task(s) from todo list.");

                emailMessages.add(em);

                tempEmailBody = "";
                tempUserName = task.getUsername();
            }

            tempEmailBody = tempEmailBody + task.getTask() + "</br>";

            if (uncompletedTask.size() < 2) {
                EmailMessage em = new EmailMessage();
                em.setEmail("paras.bhandari@idreamsoftek.com");
                em.setBody(tempEmailBody);
                em.setSubject("Your uncompleted task(s) from todo list.");

                emailMessages.add(em);
            }
        }

        return emailMessages;
    }
}
