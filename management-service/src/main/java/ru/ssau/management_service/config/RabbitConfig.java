package ru.ssau.management_service.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Value("${rabbit.queue}")
    private String rabbitQueue;

    @Bean
    public Queue analyticsQueue() {
        return new Queue(rabbitQueue, true);
    }
}
