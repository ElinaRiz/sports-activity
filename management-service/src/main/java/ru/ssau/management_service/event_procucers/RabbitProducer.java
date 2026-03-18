package ru.ssau.management_service.event_procucers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class RabbitProducer {
    @Value("${rabbit.queue}")
    private String rabbitQueue;

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public void sendEvent(String event, Map<String, Object> data) {
        try {
            String message = objectMapper.writeValueAsString(Map.of("event_type", event, "data", data));
            rabbitTemplate.convertAndSend(rabbitQueue, message);
            System.out.println("Отправлено событие: " + event);
        } catch (JsonProcessingException e) {
            System.err.println("Ошибка! Не удалось сериализовать сообщение: " + e.getMessage());
        }
    }
}