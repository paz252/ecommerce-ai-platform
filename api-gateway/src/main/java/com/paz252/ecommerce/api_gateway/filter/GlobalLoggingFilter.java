package com.paz252.ecommerce.api_gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/*
Cross-Cutting Concern: Reactive Logging Filter
Since the gateway runs on a reactive, non-blocking Netty engine instead of TomCat, 
standard Spring AOP intercepts standard thread flows poorly. 
Instead, we use Spring Cloud Gateway's GlobalFilter pipeline to calculate performance 
metrics and log execution tracing globally across microservice calls.
*/

@Component
@Slf4j
public class GlobalLoggingFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        long startTime = System.currentTimeMillis();

        log.info("Incoming Gateway Request: [Method: {}] | [Path: {}] | [Remote Address: {}]",
                request.getMethod(), request.getPath(), request.getRemoteAddress());

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            ServerHttpResponse response = exchange.getResponse();
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;

            log.info("Outgoing Gateway Response: [Path: {}] | [Status Code: {}] | [Execution Time: {}ms]",
                    request.getPath(), response.getStatusCode(), executionTime);
        }));
    }

    public int getOrder() {
        // High priority order initialization to wrap the entire request/response block
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
