package com.kyra.notification_service.api.dto;

import java.time.LocalDateTime;

public record NotificationResponse(
        String transactionId,
        String status,
        LocalDateTime timestamp
) {
}
