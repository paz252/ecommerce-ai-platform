package com.paz252.ecommerce.notification_service.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.paz252.ecommerce.notification_service.dto.NotificationMockPayload;
import com.paz252.ecommerce.notification_service.event.OrderPlacedEvent;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

// Process events incoming via Kafka asynchronously, translating payload elements into reactive outbound WebClient tracking routines.

@Service
@RequiredArgsConstructor
public class OrderPlacedConsumer {

    private final WebClient externalWebClient;

    @KafkaListener(topics = "order-placed-topic", groupId = "notification-group")
    public void consumeOrderEvent(OrderPlacedEvent event){
        // Construct mock outbound content
        NotificationMockPayload payload = new NotificationMockPayload(
            "Order Confirmation - " + event.orderNumber(),
            String.format("Hello! Your purchase of %d x SKU: %s was verified. Total charged: $%s", 
                        event.quantity(), event.productSku(), event.totalPrice()),
                        101L
        );

        // Dispatch via non-blocking WebClient pipeline
        externalWebClient.post()
            .uri("/posts")
            .body(Mono.just(payload), NotificationMockPayload.class)
            .retrieve()
            .bodyToMono(String.class)
            .subscribe();
    }

}
