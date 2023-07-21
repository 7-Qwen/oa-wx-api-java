package com.wen.oawxapi.common.config;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: 7wen
 * @Date: 2023-07-21 17:18
 * @description:
 */
@Configuration
public class RabbitMQConfig {
    @Bean
    public ConnectionFactory getFactory() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("10.0.0.130");
        connectionFactory.setPort(5672);
        return connectionFactory;
    }
}
