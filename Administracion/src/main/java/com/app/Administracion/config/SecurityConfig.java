package com.app.Administracion.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public FilterRegistrationBean<GatewaySecretFilter> disableGatewayFilterAuto(GatewaySecretFilter filter) {
        FilterRegistrationBean<GatewaySecretFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    public FilterRegistrationBean<RoleHeaderFilter> disableRoleFilterAuto(RoleHeaderFilter filter) {
        FilterRegistrationBean<RoleHeaderFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   GatewaySecretFilter gatewaySecretFilter,
                                                   RoleHeaderFilter roleHeaderFilter) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(
                            "/h2-console/**",
                            "/actuator/**",
                            "/swagger-ui/**",
                            "/swagger-ui.html",
                            "/v3/api-docs/**",
                            "/holaAdministracion"
                    ).permitAll()
                    .anyRequest().authenticated())
            .addFilterBefore(gatewaySecretFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(roleHeaderFilter, GatewaySecretFilter.class);
        return http.build();
    }
}
