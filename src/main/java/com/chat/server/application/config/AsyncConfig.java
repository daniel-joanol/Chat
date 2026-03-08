package com.chat.server.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableScheduling
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {
  
  @Override
  @Bean
  public ThreadPoolTaskExecutor getAsyncExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    int availableProcessors = Runtime.getRuntime().availableProcessors();
    executor.setCorePoolSize(availableProcessors);
    executor.setMaxPoolSize(availableProcessors * 2);
    executor.setQueueCapacity(5);
    executor.setThreadNamePrefix("async-");
    executor.initialize();
    return executor;
  }

}
