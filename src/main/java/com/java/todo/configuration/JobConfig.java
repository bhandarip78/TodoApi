package com.java.todo.configuration;

import com.java.todo.job.UnCompleteTaskNotificationJob;
import com.java.todo.job.DataToSendEmail;
import com.java.todo.model.EmailMessage;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;

@Configuration
@EnableScheduling
public class JobConfig implements SchedulingConfigurer {
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();

        threadPoolTaskScheduler.setPoolSize(10);
        threadPoolTaskScheduler.setThreadNamePrefix("todo-job-task-pool::");
        threadPoolTaskScheduler.initialize();

        taskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
    }

    @Bean("todoSchedulerFactoryBean")
    public SchedulerFactoryBean todoSchedulerFactoryBean(DataSource dataSource) {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        Properties properties = new Properties();
        properties.setProperty("org.quartz.threadPool.threadNamePrefix", "todo-scheduler_thread");
        factory.setQuartzProperties(properties);
        factory.setDataSource(dataSource);
        return factory;
    }

    @Bean("todoScheduler")
    public Scheduler todoScheduler(@Qualifier("todoSchedulerFactoryBean") SchedulerFactoryBean factory) throws SchedulerException {
        Scheduler scheduler = factory.getScheduler();
        scheduler.start();
        return scheduler;
    }

    @Bean
    public CommandLineRunner run(@Qualifier("todoScheduler") Scheduler todoScheduler, @Qualifier("dataToSendEmail") DataToSendEmail dataToSendEmail) {
        return (String[] args) -> {

            System.out.println("HEL0000000000000000000");
            List<EmailMessage> emailMessages = dataToSendEmail.getMessageList();

            if (emailMessages != null & emailMessages.size() > 0) {
                System.out.println("Uncomplete Task found");
            }
            else {
                System.out.println("Uncomplete Task NOT found");
            }

            for (EmailMessage emailMessage:
                 emailMessages) {
                JobDetail jobDetail = createJobDetail(emailMessage);

                SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder
                        .simpleSchedule()
                        .withIntervalInSeconds(5)
                        .repeatForever();

                Trigger trigger = TriggerBuilder
                        .newTrigger()
                        .withSchedule(scheduleBuilder)
                        .build();

                todoScheduler.scheduleJob(jobDetail, trigger);
            }
        };
    }

    private JobDetail createJobDetail(EmailMessage emailMessage) {
        JobDataMap jobDataMap = new JobDataMap();

        jobDataMap.put("email", emailMessage.getEmail());
        jobDataMap.put("subject", emailMessage.getSubject());
        jobDataMap.put("body", emailMessage.getBody());
        jobDataMap.put("jobTriggerTime", LocalDateTime.now().toString());

        return JobBuilder.newJob(UnCompleteTaskNotificationJob.class)
                .usingJobData(jobDataMap)
                .build();
    }
}
