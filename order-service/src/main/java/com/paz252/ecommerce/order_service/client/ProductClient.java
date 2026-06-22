package com.paz252.ecommerce.order_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.paz252.ecommerce.order_service.dto.ProductResponse;

// This client interacts transparently with the product-service cluster by finding physical nodes from Eureka.

@FeignClient(name = "product-service", path = "/products")
public interface ProductClient {

    @GetMapping("/{id}")
    ProductResponse getProductById(@PathVariable String id);
}
