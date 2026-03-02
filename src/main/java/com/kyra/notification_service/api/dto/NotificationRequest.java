package com.kyra.notification_service.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NotificationRequest(
        @NotBlank(message = "userId is required")
        String userId,

        @NotNull(message = "messageType is required")
        MessageType type,

        @NotBlank(message = "destination is required")
        String destination,

        @NotBlank(message = "content is required")
        @Size(max = 1000, message = "Message too long")
        String content,

        @NotNull(message = "priority is required")
        Priority priority,

        String correlationId,

        String templateId
) {
}
