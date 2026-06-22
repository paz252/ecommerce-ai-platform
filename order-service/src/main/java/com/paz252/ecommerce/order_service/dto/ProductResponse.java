package com.paz252.ecommerce.order_service.dto;

import java.math.BigDecimal;

// Matches the model from product-service for parsing Feign responses

public record ProductResponse(
    String id,
    String name,
    BigDecimal price,
    Integer stockQuantity
) {}
