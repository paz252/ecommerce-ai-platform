package com.paz252.ecommerce.order_service.event;

import java.math.BigDecimal;

public record OrderPlacedEvent(
    String orderId,
    String productId,
    Integer quantity,
    BigDecimal totalAmount,
    String customerEmail
) {}
