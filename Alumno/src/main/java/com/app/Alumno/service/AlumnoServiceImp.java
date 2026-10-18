package com.app.Alumno.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.Alumno.Enum.Estado;
import com.app.Alumno.dto.AlumnoDTO;
import com.app.Alumno.entity.Alumno;
import com.app.Alumno.exception.ResourceNotFoundException;
import com.app.Alumno.mappers.AlumnoMapper;
import com.app.Alumno.repository.Alumnorepository;
import com.app.Alumno.senders.AlumnoEventSender;

@Service
public class AlumnoServiceImp implements AlumnoService {

    @Autowired
    private Alumnorepository alumnorepository;

    @Autowired
    private AlumnoEventSender alumnoEventSender;

    @Autowired
    private AlumnoMapper alumnoMapper;

    @Override
    public List<AlumnoDTO> listar() {
        return alumnorepository.findAll().stream()
                .map(alumnoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AlumnoDTO guardar(AlumnoDTO dto) {
        Alumno entidad;
        if (dto.getId() != null) {
            entidad = alumnorepository.findById(dto.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Alumno no encontrado: " + dto.getId()));
            entidad.setNombre(dto.getNombre());
            entidad.setApellido(dto.getApellido());
            entidad.setGmail(dto.getGmail());
            entidad.setCursoId(dto.getCursoId());
            if (dto.getEstado() != null) {
                entidad.setEstado(dto.getEstado());
            }
            if (dto.getCursoId() == null) {
                entidad.setEstado(Estado.SIN_ASIGNAR);
            } else if (entidad.getEstado() == Estado.SIN_ASIGNAR || entidad.getEstado() == Estado.NOHABILITADO) {
                if (dto.getEstado() == null || dto.getEstado() == Estado.SIN_ASIGNAR) {
                    entidad.setEstado(Estado.HABILITADO);
                }
            }
        } else {
            entidad = alumnoMapper.toEntity(dto);
            if (entidad.getEstado() == null) {
                entidad.setEstado(dto.getCursoId() == null ? Estado.SIN_ASIGNAR : Estado.HABILITADO);
            }
        }
        AlumnoDTO saved = alumnoMapper.toDto(alumnorepository.save(entidad));
        alumnoEventSender.notificarCambioAlumno(saved);
        return saved;
    }

    @Override
    public AlumnoDTO cambiarEstado(Long id, String estado) {
        Alumno alumno = alumnorepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alumno no encontrado: " + id));
        Estado nuevoEstado;
        try {
            nuevoEstado = Estado.valueOf(estado.toUpperCase());
        } catch (IllegalArgumentException ex) {
            nuevoEstado = Estado.NOHABILITADO;
        }
        alumno.setEstado(nuevoEstado);
        AlumnoDTO saved = alumnoMapper.toDto(alumnorepository.save(alumno));
        alumnoEventSender.notificarCambioAlumno(saved);
        return saved;
    }
}
