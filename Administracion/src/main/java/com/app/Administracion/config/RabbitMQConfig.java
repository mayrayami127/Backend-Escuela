package com.app.Administracion.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_ALUMNOS = "escuela.admin.alumnos.queue";
    public static final String EXCHANGE_ESCUELA = "escuela.exchange";
    public static final String ROUTING_KEY_ALUMNOS = "escuela.alumnos.routingkey";

    @Bean
    public Queue adminAlumnosQueue() {
        return new Queue(QUEUE_ALUMNOS, true);
    }

    @Bean
    public TopicExchange escuelaExchange() {
        return new TopicExchange(EXCHANGE_ESCUELA);
    }

    @Bean
    public Binding adminAlumnosBinding(Queue adminAlumnosQueue, TopicExchange escuelaExchange) {
        return BindingBuilder.bind(adminAlumnosQueue).to(escuelaExchange).with(ROUTING_KEY_ALUMNOS);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
