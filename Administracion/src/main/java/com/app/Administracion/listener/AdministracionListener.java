package com.app.Administracion.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.app.Administracion.config.RabbitMQConfig;
import com.app.Administracion.dto.AlumnoEventoDto;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AdministracionListener {

    private static final Logger log = LoggerFactory.getLogger(AdministracionListener.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @RabbitListener(queues = RabbitMQConfig.QUEUE_ALUMNOS)
    public void consumirEventoAlumno(Message message) throws Exception {
        AlumnoEventoDto evento = objectMapper.readValue(message.getBody(), AlumnoEventoDto.class);
        log.info("Procesando evento asincrónico vía RabbitMQ: {} {} (estado={})",
                evento.getNombre(), evento.getApellido(), evento.getEstado());
    }
}
