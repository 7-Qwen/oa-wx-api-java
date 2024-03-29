package com.wen.oawxapi.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author: 7wen
 * @Date: 2023-07-05 20:52
 * @description: 创建异步多线程配置类进入springboot
 */
@Configuration
public class ThreadPoolConfig {
    @Bean
    public AsyncTaskExecutor asyncTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //设置核心线程数
        executor.setCorePoolSize(8);
        //设置最大线程池数
        executor.setMaxPoolSize(16);
        //设置队列容量
        executor.setQueueCapacity(32);
        //设置线程活跃时长(秒)
        executor.setKeepAliveSeconds(60);
        //设置默认线程名称
        executor.setThreadNamePrefix("task-");
        //设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

}
