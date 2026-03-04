package com.kyra.notification_service.core.service;

import com.kyra.notification_service.api.dto.NotificationRequest;
import com.kyra.notification_service.core.correlation.CorrelationIdProvider;
import com.kyra.notification_service.core.event.NotificationEvent;
import com.kyra.notification_service.core.event.NotificationStatus;
import com.kyra.notification_service.provider.NotificationProducer;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class NotificationService {

    private final CorrelationIdProvider correlationIdProvider;
    private final NotificationProducer notificationProducer;

    public NotificationService(
            CorrelationIdProvider correlationIdProvider,
            NotificationProducer notificationProducer
    ) {
        this.correlationIdProvider = correlationIdProvider;
        this.notificationProducer = notificationProducer;
    }

    public NotificationEvent createNotificationEvent(NotificationRequest request) {
        String eventId = UUID.randomUUID().toString();
        NotificationEvent event = new NotificationEvent(
                eventId,
                correlationIdProvider.getOrCreate(),
                request.userId(),
                request.type(),
                request.destination(),
                request.content(),
                request.priority(),
                request.templateId(),
                NotificationStatus.PENDING,
                LocalDateTime.now(),
                0
        );

        notificationProducer.send(event);
        return event;
    }
}
