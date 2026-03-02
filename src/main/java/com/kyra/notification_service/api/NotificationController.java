package com.kyra.notification_service.api;

import com.kyra.notification_service.api.dto.NotificationRequest;
import com.kyra.notification_service.api.dto.NotificationResponse;
import com.kyra.notification_service.core.event.NotificationEvent;
import com.kyra.notification_service.core.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<NotificationResponse> createNotification(
            @Valid @RequestBody NotificationRequest request
    ) {
        NotificationEvent event = notificationService.createNotificationEvent(request);
        NotificationResponse response = new NotificationResponse(
                event.correlationId(),
                event.status().name(),
                event.createdAt()
        );
        return ResponseEntity.accepted().body(response);
    }
}
