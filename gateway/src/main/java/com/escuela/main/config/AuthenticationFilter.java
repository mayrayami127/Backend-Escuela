package com.escuela.main.config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final JwtUtil jwtUtil;

    public AuthenticationFilter(JwtUtil jwtUtil) {
        super(Config.class);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
                return chain.filter(exchange);
            }
            String header = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (header == null || !header.startsWith("Bearer ")) {
                return unauthorized(exchange);
            }
            try {
                Claims claims = jwtUtil.validate(header.substring(7));
                ServerHttpRequest request = exchange.getRequest().mutate()
                        .header("X-User", claims.getSubject()).build();
                return chain.filter(exchange.mutate().request(request).build());
            } catch (JwtException | IllegalArgumentException e) {
                return unauthorized(exchange);
            }
        };
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    public static class Config {}
}