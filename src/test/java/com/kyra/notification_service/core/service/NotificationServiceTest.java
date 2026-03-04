package com.kyra.notification_service.core.service;

import com.kyra.notification_service.api.dto.MessageType;
import com.kyra.notification_service.api.dto.NotificationRequest;
import com.kyra.notification_service.api.dto.Priority;
import com.kyra.notification_service.core.correlation.CorrelationIdProvider;
import com.kyra.notification_service.core.event.NotificationEvent;
import com.kyra.notification_service.core.event.NotificationStatus;
import com.kyra.notification_service.provider.NotificationProducer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class NotificationServiceTest {

    @Test
    void createNotificationEvent_shouldPopulateGeneratedFieldsAndRequest() {
        CorrelationIdProvider correlationIdProvider = mock(CorrelationIdProvider.class);
        NotificationProducer notificationProducer = mock(NotificationProducer.class);
        when(correlationIdProvider.getOrCreate()).thenReturn("corr-123");
        NotificationService notificationService = new NotificationService(correlationIdProvider, notificationProducer);

        NotificationRequest request = new NotificationRequest(
                "user-123",
                MessageType.EMAIL,
                "test@example.com",
                "Hello",
                Priority.HIGH,
                null,
                "template-1"
        );

        NotificationEvent event = notificationService.createNotificationEvent(request);

        assertNotNull(event.eventId());
        assertNotNull(event.createdAt());
        assertEquals("corr-123", event.correlationId());
        assertEquals("user-123", event.userId());
        assertEquals(MessageType.EMAIL, event.type());
        assertEquals("test@example.com", event.destination());
        assertEquals("Hello", event.content());
        assertEquals(Priority.HIGH, event.priority());
        assertEquals("template-1", event.templateId());
        assertEquals(NotificationStatus.PENDING, event.status());
        assertEquals(0, event.retryCount());
        verify(notificationProducer).send(event);
    }
}
