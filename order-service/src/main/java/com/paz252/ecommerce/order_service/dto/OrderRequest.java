package com.paz252.ecommerce.order_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrderRequest(
    @NotBlank(message = "Product ID cannot be blank")
    String productId,

    @NotNull(message = "Quantity is required")
    Integer quantity,

    @NotBlank(message = "Customer email cannot be blank")
    String customerEmail
) {}
