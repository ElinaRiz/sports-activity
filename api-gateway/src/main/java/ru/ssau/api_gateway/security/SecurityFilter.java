package ru.ssau.api_gateway.security;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.ssau.api_gateway.dto.UserDataResponse;

@Component
public class SecurityFilter implements GlobalFilter, Ordered {
    private final WebClient client;

    public SecurityFilter(WebClient.Builder builder) {
        client = builder.baseUrl("lb://auth-service").build();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        if (!path.startsWith("/api") || path.startsWith("/api/auth/security/login")  || path.startsWith("/api/auth/security/register")) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        return client.get()
                .uri("/security/role")
                .header("Authorization", authHeader)
                .retrieve()
                .bodyToMono(UserDataResponse.class)
                .flatMap(data -> {
                    ServerHttpRequest request = exchange.getRequest().mutate()
                            .header("X-Login", data.login())
                            .header("X-Role", data.role())
                            .header("X-Secret", data.secret())
                            .build();

                    return chain.filter(exchange.mutate().request(request).build());
                })
                .onErrorResume(e -> {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                });
    }

    @Override
    public int getOrder() {
        return -1;
    }
}