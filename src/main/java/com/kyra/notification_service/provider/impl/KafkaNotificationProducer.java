package com.kyra.notification_service.provider.impl;

import com.kyra.notification_service.core.event.NotificationEvent;
import com.kyra.notification_service.provider.NotificationProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Primary
@Profile("!test")
@Slf4j
public class KafkaNotificationProducer implements NotificationProducer {

    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;
    private final String topicName = "notification-events";

    public KafkaNotificationProducer(KafkaTemplate<String, NotificationEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(NotificationEvent event) {
        String key = event.correlationId();

        log.info("Sending event to Kafka topic [{}]: Key={}, EventId={}", topicName, key, event.eventId());

        kafkaTemplate.send(topicName, key, event)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info(
                                "Successfully sent message to partition: {} with offset: {}",
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset()
                        );
                    } else {
                        log.error("Unable to send message=[{}] due to : {}", event, ex.getMessage());
                    }
                });
    }
}
