package com.kyra.notification_service.provider.impl;

import com.kyra.notification_service.core.event.NotificationEvent;
import com.kyra.notification_service.provider.NotificationProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Primary
@Profile("test")
public class MockNotificationProducer implements NotificationProducer {

    private static final Logger log = LoggerFactory.getLogger(MockNotificationProducer.class);

    @Override
    public void send(NotificationEvent event) {
        log.info("Mock producer received eventId={} correlationId={}", event.eventId(), event.correlationId());
    }
}
