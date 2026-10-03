package com.app.Alumno.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.Alumno.dto.AlumnoDTO;
import com.app.Alumno.entity.Alumno;
import com.app.Alumno.repository.Alumnorepository;

@Service
public class AlumnoServiceImp implements AlumnoService {

    @Autowired
    private Alumnorepository alumnorepository;

    @Override
    public List<AlumnoDTO> listar() {
        return alumnorepository.findAll().stream()
                .map(this::aDto)
                .collect(Collectors.toList());
    }

    @Override
    public void agregar(AlumnoDTO dto) {
        alumnorepository.save(aEntidad(dto));
    }

    @Override
    public boolean eliminar(int id) {
        if (!alumnorepository.existsById(id)) {
            return false;
        }
        alumnorepository.deleteById(id);
        return true;
    }

    // ---- Mapeo entidad <-> DTO ----
    private AlumnoDTO aDto(Alumno entidad) {
        AlumnoDTO dto = new AlumnoDTO();
        dto.setId(entidad.getId());
        dto.setNombre(entidad.getNombre());
        dto.setApellido(entidad.getApellido());
        dto.setGmail(entidad.getGmail());
        dto.setCursoId(entidad.getCursoId());
        return dto;
    }

    private Alumno aEntidad(AlumnoDTO dto) {
        Alumno entidad = new Alumno();
        entidad.setId(dto.getId());
        entidad.setNombre(dto.getNombre());
        entidad.setApellido(dto.getApellido());
        entidad.setGmail(dto.getGmail());
        entidad.setCursoId(dto.getCursoId());
        return entidad;
    }
}
