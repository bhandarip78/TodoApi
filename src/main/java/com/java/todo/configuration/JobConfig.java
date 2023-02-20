package com.java.todo.configuration;

import com.java.todo.job.DataToSendEmailJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
import java.util.Properties;

@Configuration
@EnableScheduling
public class JobConfig implements SchedulingConfigurer {
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();

        threadPoolTaskScheduler.setPoolSize(10);
        threadPoolTaskScheduler.setThreadNamePrefix("todo-job-task-pool::");
        threadPoolTaskScheduler.initialize();

        taskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
    }

    @Bean("uncompleteTodoDataSchedulerFactory")
    public SchedulerFactoryBean uncompleteTodoDataSchedulerFactory(DataSource dataSource) {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();

        Properties properties = new Properties();
        properties.setProperty("org.quartz.threadPool.threadNamePrefix", "todo-uncomplete-scheduler_thread");
        factory.setQuartzProperties(properties);
        factory.setDataSource(dataSource);

        //Custom job factory of spring with DI support for @Autowired!
        AutowiringJobFactory jobFactory = new AutowiringJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        factory.setJobFactory(jobFactory);

        return factory;
    }

    @Bean("uncompleteTodoScheduler")
    public Scheduler uncompleteTodoScheduler(@Qualifier("uncompleteTodoDataSchedulerFactory") SchedulerFactoryBean factory) throws SchedulerException {
        Scheduler scheduler = factory.getScheduler();
        scheduler.start();
        return scheduler;
    }

    @Bean
    public CommandLineRunner run(@Qualifier("uncompleteTodoScheduler") Scheduler uncompleteTodoScheduler) {
        return (String[] args) -> {
            SimpleScheduleBuilder scheduleBuilder1 = SimpleScheduleBuilder
                    .simpleSchedule()
                    .withIntervalInSeconds(5)
                    .repeatForever();

            Trigger trigger = TriggerBuilder
                    .newTrigger()
                    .withSchedule(scheduleBuilder1)
                    .build();

            JobDetail jobDetail = JobBuilder.newJob(DataToSendEmailJob.class)
                    .build();

            uncompleteTodoScheduler.scheduleJob(jobDetail, trigger);
        };
    }
}
