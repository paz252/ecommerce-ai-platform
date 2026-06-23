package com.paz252.ecommerce.notification_service.event;

import java.math.BigDecimal;

// Map the incoming data signature emitted by your upstream order context matching standard DTO models exactly.

public record OrderPlacedEvent(
    String orderNumber,
    String productSku,
    Integer quantity,
    BigDecimal totalPrice,
    String customerEmail
) {
}
