package com.java.todo.job;

import com.java.todo.model.EmailMessage;
import com.java.todo.model.Todo;
import com.java.todo.service.TodoService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component()
@PropertySource("classpath:application.properties")
public class DataToSendEmailJob implements Job {
    private final static Logger logger = LoggerFactory.getLogger(DataToSendEmailJob.class);

    // Temporary solution. In real-world, you would get client email from client profile.
    @Value("${client-to-email}")
    private String clientToEmail;

    @Autowired
    private TodoService todoService;

    @Autowired
    private @Qualifier("uncompleteTodoScheduler") Scheduler uncompleteTodoScheduler;

    @Override
    public void execute(JobExecutionContext context){
        try {
            List<EmailMessage> emailMessages = getMessageList();
            if (emailMessages != null & emailMessages.size() > 0) {
                logger.debug("DataToSendEmailJob::Uncomplete Task found");
                logger.debug(MessageFormat.format("DataToSendEmailJob::execute:: emailMessage size - {0}", emailMessages.size()));
            }
            else {
                logger.debug("DataToSendEmailJob::Uncomplete Task NOT found");
            }

            for (EmailMessage emailMessage:
                    emailMessages) {
                logger.debug("DataToSendEmailJob:: inside for loop");
                JobDetail jobDetail = createJobDetail(emailMessage);

                Trigger trigger = TriggerBuilder
                        .newTrigger()
                        .build();

                uncompleteTodoScheduler.scheduleJob(jobDetail, trigger);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<EmailMessage> getMessageList() {
        return getEmailMessages();
    }

    private List<EmailMessage> getEmailMessages() {
        logger.debug(("DataToSendEmailJob::getEmailMessage method"));
        List<EmailMessage> emailMessages = new ArrayList<>();

        List<Todo> uncompletedTask = todoService.getUncompletedTasks();

        if (uncompletedTask.size() > 0) {
            logger.debug("DatatoSendEmailJob::uncompleted task found");
            logger.debug(MessageFormat.format("DataToSendEmail::List size - {0}", uncompletedTask.size()));
        }
        else {
            logger.debug("DataToSendEmailJob::uncompleted task Notfound");
        }

        uncompletedTask.sort(Comparator.comparing(Todo::getUsername));
        logger.debug(MessageFormat.format("DataToSendEmailJob::List size after sort - {0}", uncompletedTask.size()));

        String tempUserName = null;
        String tempEmailBody = "";
        int index = 1;
        for (Todo task : uncompletedTask) {
            if (tempUserName == null) {
                tempUserName = task.getUsername();
            }

            // Use case - when uncompleted tasks list contains data for different users.
            if (!tempUserName.equals(task.getUsername())) {
                addEmailMessageToList(emailMessages, tempEmailBody);

                tempEmailBody = "";
                tempUserName = task.getUsername();
            }

            tempEmailBody = tempEmailBody + task.getTask() + "</br>";

            // Use case - Only one uncompleted task found.
            if (uncompletedTask.size() < 2) {
                addEmailMessageToList(emailMessages, tempEmailBody);
            }

            // Use case - after the last uncompleted task from list.
            if (index == uncompletedTask.size()) {
                addEmailMessageToList(emailMessages, tempEmailBody);
            }
            ++index;
        }

        return emailMessages;
    }

    private void addEmailMessageToList(List<EmailMessage> emailMessages, String body) {
        EmailMessage em = new EmailMessage();
        em.setEmail(clientToEmail);
        em.setBody(body);
        em.setSubject("Your uncompleted task(s) from todo list.");

        emailMessages.add(em);
    }

    private JobDetail createJobDetail(EmailMessage emailMessage) {
        JobDataMap jobDataMap = new JobDataMap();

        jobDataMap.put("email", emailMessage.getEmail());
        jobDataMap.put("subject", emailMessage.getSubject());
        jobDataMap.put("body", emailMessage.getBody());
        jobDataMap.put("jobTriggerTime", LocalDateTime.now().toString());

        logger.debug(MessageFormat.format("DataToSendEmailJob::createJobDetail:: Email: {0} | Subject: {1} | Body: {2}", emailMessage.getEmail(), emailMessage.getSubject(), emailMessage.getBody()));

        return JobBuilder.newJob(UnCompleteTaskNotificationJob.class)
                .usingJobData(jobDataMap)
                .build();
    }
}
