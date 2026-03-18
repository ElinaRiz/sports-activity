package ru.ssau.management_service.event_procucers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.ssau.management_service.dto.event.ActivityCreatedEvent;

@Service
@Slf4j
public class AnalyticsServiceProducer {
    private final WebClient client;

    public AnalyticsServiceProducer(WebClient.Builder builder) {
        client = builder.baseUrl("lb://analytics-service").build();
    }

    public void sendActivityCreatedEvent(ActivityCreatedEvent event) {
        client.post().uri("/event/activity_created")
                .bodyValue(event)
                .retrieve()
                .toBodilessEntity()
                .doOnSuccess(r -> log.info("Событие activity_created отправлено"))
                .doOnError(e -> log.error("Ошибка при отправке activity_created", e))
                .subscribe(null, e -> log.error("Ошибка подписки analytics", e));
    }
}