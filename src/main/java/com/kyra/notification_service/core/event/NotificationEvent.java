package com.kyra.notification_service.core.event;

import com.kyra.notification_service.api.dto.MessageType;
import com.kyra.notification_service.api.dto.Priority;

import java.time.LocalDateTime;

public record NotificationEvent(
        String eventId,
        String correlationId,
        String userId,
        MessageType type,
        String destination,
        String content,
        Priority priority,
        String templateId,
        NotificationStatus status,
        LocalDateTime createdAt,
        int retryCount
) {
}
