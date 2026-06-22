package com.paz252.ecommerce.product_service.dto;

import java.io.Serializable;
import java.math.BigDecimal;

// Implements Serializable so it can be cached cleanly by Redis
public record ProductResponse(
    Long id,
    String name,
    String description,
    BigDecimal price,
    Integer stockQuantity
) implements Serializable {}
