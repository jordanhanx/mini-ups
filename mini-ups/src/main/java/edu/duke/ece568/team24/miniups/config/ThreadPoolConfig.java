package edu.duke.ece568.team24.miniups.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableScheduling
public class ThreadPoolConfig {

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4); // Set the core pool size
        executor.setMaxPoolSize(4); // Set the maximum pool size
        executor.setQueueCapacity(100); // Set the queue capacity
        executor.setThreadNamePrefix("MyRecvThread-"); // Set the thread name prefix
        executor.initialize(); // Initialize the executor
        return executor;
    }

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(2); // Set the pool size
        scheduler.setThreadNamePrefix("MySendScheduler-"); // Set the thread name prefix
        scheduler.initialize(); // Initialize the scheduler
        return scheduler;
    }
}
