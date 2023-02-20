package com.java.todo.job;

import com.java.todo.service.TodoEmailService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class UnCompleteTaskNotificationJob implements Job {
    private final static Logger logger = LoggerFactory.getLogger(UnCompleteTaskNotificationJob.class);

    @Autowired
    private TodoEmailService todoEmailService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException{
        try {
            logger.debug("UnCompleteTaskNotificationJob:: Inside execute method");
            JobDataMap dataMap = context.getJobDetail().getJobDataMap();
            String param = dataMap.getString("jobTriggerTime");
            String recipientEmail = dataMap.getString("email");
            String subject = dataMap.getString("subject");
            String body = dataMap.getString("body");
            System.out.println(MessageFormat.format("Job: {0} | TriggeredAt: {1} | Thread: {2}",
                    getClass(), param, Thread.currentThread().getName()));

            logger.debug(MessageFormat.format("Email: {0} | Subject: {1} | Body: {2}", recipientEmail, subject, body));

            todoEmailService.sendMessage(recipientEmail, subject, body);
            logger.debug("Successfully sent email");
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
