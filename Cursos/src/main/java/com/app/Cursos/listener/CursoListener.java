package com.app.Cursos.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.Cursos.config.RabbitMQConfig;
import com.app.Cursos.dto.AlumnoEventoDto;
import com.app.Cursos.service.InscripcionService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CursoListener {

    private static final Logger log = LoggerFactory.getLogger(CursoListener.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private InscripcionService inscripcionService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_ALUMNOS)
    public void recibirEventoAlumno(Message message) throws Exception {
        AlumnoEventoDto event = objectMapper.readValue(message.getBody(), AlumnoEventoDto.class);
        log.info("Evento alumno recibido en Cursos: id={}, cursoId={}, estado={}",
                event.getId(), event.getCursoId(), event.getEstado());
        inscripcionService.procesarEventoAlumno(event);
    }
}
