package com.app.Cursos.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.Cursos.dto.CursoDTO;
import com.app.Cursos.entity.Cursos;
import com.app.Cursos.repository.CursosRepository;

@Service
public class CursosServiceImp implements CursosService {

    @Autowired
    private CursosRepository cursosRepository;

    @Override
    public List<CursoDTO> listar() {
        return cursosRepository.findAll().stream()
                .map(this::aDto)
                .collect(Collectors.toList());
    }

    @Override
    public void agregar(CursoDTO dto) {
        cursosRepository.save(aEntidad(dto));
    }

    @Override
    public boolean eliminar(int id) {
        if (!cursosRepository.existsById(id)) {
            return false;
        }
        cursosRepository.deleteById(id);
        return true;
    }

    // ---- Mapeo entidad <-> DTO ----
    private CursoDTO aDto(Cursos entidad) {
        CursoDTO dto = new CursoDTO();
        dto.setId(entidad.getId());
        dto.setMateria(entidad.getMateria());
        dto.setNombreMaestro(entidad.getNombreMaestro());
        dto.setNumSalon(entidad.getNumSalon());
        dto.setAdministracionId(entidad.getAdministracionId());
        return dto;
    }

    private Cursos aEntidad(CursoDTO dto) {
        Cursos entidad = new Cursos();
        entidad.setId(dto.getId());
        entidad.setMateria(dto.getMateria());
        entidad.setNombreMaestro(dto.getNombreMaestro());
        entidad.setNumSalon(dto.getNumSalon());
        entidad.setAdministracionId(dto.getAdministracionId());
        return entidad;
    }
}
