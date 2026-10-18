package com.app.Alumno.senders;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.Alumno.config.RabbitMQConfig;
import com.app.Alumno.dto.AlumnoDTO;

@Component
public class AlumnoEventSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void notificarCambioAlumno(AlumnoDTO dto) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_ESCUELA,
                RabbitMQConfig.ROUTING_KEY_ALUMNOS,
                dto);
    }
}
