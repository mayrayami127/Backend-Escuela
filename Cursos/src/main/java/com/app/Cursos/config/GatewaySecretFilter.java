package com.app.Cursos.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Solo acepta pedidos que vengan del gateway (header X-Gateway-Secret).
 * La documentación (Swagger) queda abierta para poder verla; los endpoints del CRUD siguen protegidos.
 */
@Component
public class GatewaySecretFilter extends OncePerRequestFilter {

    @Value("${gateway.internal-secret}")
    private String secret;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String p = request.getRequestURI();
        return p.startsWith("/h2-console")
                || p.startsWith("/actuator")
                || p.startsWith("/swagger-ui")
                || p.startsWith("/v3/api-docs");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        if (!secret.equals(req.getHeader("X-Gateway-Secret"))) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Solo se puede acceder a través del gateway");
            return;
        }
        chain.doFilter(req, res);
    }
}
