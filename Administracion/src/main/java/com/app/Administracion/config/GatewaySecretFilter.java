package com.app.Administracion.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class GatewaySecretFilter extends OncePerRequestFilter {

    @Value("${gateway.internal-secret}")
    private String secret;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String p = request.getRequestURI();
        return p.startsWith("/h2-console") || p.startsWith("/actuator");
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