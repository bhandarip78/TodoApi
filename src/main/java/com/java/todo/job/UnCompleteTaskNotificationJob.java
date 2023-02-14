package com.java.todo.job;

import com.java.todo.service.TodoEmailService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class UnCompleteTaskNotificationJob implements Job {

    @Autowired
    private TodoEmailService todoEmailService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException{
        try {
            JobDataMap dataMap = context.getJobDetail().getJobDataMap();
            String param = dataMap.getString("jobTriggerTime");
            String recipientEmail = dataMap.getString("email");
            String subject = dataMap.getString("subject");
            String body = dataMap.getString("body");
            System.out.println(MessageFormat.format("Job: {0} | TriggeredAt: {1} | Thread: {2}",
                    getClass(), param, Thread.currentThread().getName()));

            System.out.println(MessageFormat.format("Email: {0} | Subject: {1} | Body: {2}", recipientEmail, subject, body));

            todoEmailService.sendMessage(recipientEmail, subject, body);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
