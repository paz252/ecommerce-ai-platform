package com.paz252.ecommerce.order_service.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.paz252.ecommerce.order_service.client.ProductClient;
import com.paz252.ecommerce.order_service.dto.OrderRequest;
import com.paz252.ecommerce.order_service.dto.ProductResponse;
import com.paz252.ecommerce.order_service.event.OrderPlacedEvent;
import com.paz252.ecommerce.order_service.model.Order;
import com.paz252.ecommerce.order_service.repository.OrderRepository;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    @Transactional
    public String placeOrder(OrderRequest orderRequest) {

        // 1. Fetch details synchronously from Product Service via Feign
        ProductResponse product = productClient.getProductById(orderRequest.productId());

        if (product.stockQuantity() < orderRequest.quantity()) {
            throw new RuntimeException("Insufficient inventory available for Product ID: " + orderRequest.productId());
        }

        // 2. Map and persist order details
        BigDecimal totalAmount = product.price().multiply(BigDecimal.valueOf(orderRequest.quantity()));
        Order order = Order.builder()
                .productId((orderRequest.productId()))
                .quantity(orderRequest.quantity())
                .customerEmail(orderRequest.customerEmail())
                .totalAmount(totalAmount)
                .orderTime(LocalDateTime.now())
                .build();

        Order savedOrder = orderRepository.save(order);

        // 3. Fire-and-forget Event Stream publication onto Kafka
        OrderPlacedEvent event = new OrderPlacedEvent(
                savedOrder.getId(),
                savedOrder.getProductId(),
                savedOrder.getQuantity(),
                savedOrder.getTotalAmount(),
                savedOrder.getCustomerEmail());

        try {
            kafkaTemplate.send("notification-topic", event.orderId(), event);
        } catch (Exception e) {
        }

        return savedOrder.getId();
    }

}
