package com.kyra.notification_service.provider;

import com.kyra.notification_service.core.event.NotificationEvent;

public interface NotificationProducer {
    void send(NotificationEvent event);
}
