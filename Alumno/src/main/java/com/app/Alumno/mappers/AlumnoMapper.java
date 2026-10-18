package com.app.Alumno.mappers;

import org.springframework.stereotype.Component;

import com.app.Alumno.Enum.Estado;
import com.app.Alumno.dto.AlumnoDTO;
import com.app.Alumno.entity.Alumno;

@Component
public class AlumnoMapper {

    public AlumnoDTO toDto(Alumno entity) {
        if (entity == null) {
            return null;
        }
        AlumnoDTO dto = new AlumnoDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setApellido(entity.getApellido());
        dto.setGmail(entity.getGmail());
        dto.setCursoId(entity.getCursoId());
        dto.setEstado(entity.getEstado());
        return dto;
    }

    public Alumno toEntity(AlumnoDTO dto) {
        if (dto == null) {
            return null;
        }
        Alumno entity = new Alumno();
        entity.setNombre(dto.getNombre());
        entity.setApellido(dto.getApellido());
        entity.setGmail(dto.getGmail());
        entity.setCursoId(dto.getCursoId());
        entity.setEstado(dto.getEstado() != null ? dto.getEstado() : Estado.HABILITADO);
        return entity;
    }
}
