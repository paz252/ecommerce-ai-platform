package com.paz252.ecommerce.notification_service.dto;

// Define the immutable structure required by the external API target payload.

public record NotificationMockPayload(
    String title,
    String body,
    Long userId
) {
}
