package com.app.Cursos.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.Cursos.dto.CursoDTO;
import com.app.Cursos.entity.Cursos;
import com.app.Cursos.exception.ResourceNotFoundException;
import com.app.Cursos.repository.CursosRepository;

@Service
public class CursosServiceImp implements CursosService {

    @Autowired
    private CursosRepository cursosRepository;

    @Override
    public List<CursoDTO> listar() {
        return cursosRepository.findAll().stream().map(this::aDto).collect(Collectors.toList());
    }

    @Override
    public List<CursoDTO> listarParaUsuario(String email, boolean soloAsignados) {
        if (soloAsignados) {
            return cursosRepository.findByDocenteEmail(email).stream().map(this::aDto).collect(Collectors.toList());
        }
        return listar();
    }

    @Override
    public CursoDTO guardar(CursoDTO dto) {
        if (dto.getCupo() < 1) {
            throw new IllegalArgumentException("El cupo del curso debe ser al menos 1");
        }
        if (dto.getMateria() == null || dto.getMateria().isBlank()) {
            throw new IllegalArgumentException("La materia es obligatoria");
        }
        if (dto.getHorario() == null || dto.getHorario().isBlank()) {
            throw new IllegalArgumentException("El horario es obligatorio");
        }
        Cursos entidad;
        if (dto.getId() != null) {
            entidad = cursosRepository.findById(dto.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado: " + dto.getId()));
            entidad.setMateria(dto.getMateria());
            entidad.setNombreMaestro(dto.getNombreMaestro());
            entidad.setNumSalon(dto.getNumSalon());
            entidad.setCupo(dto.getCupo());
            entidad.setHorario(dto.getHorario());
            entidad.setDocenteEmail(dto.getDocenteEmail());
            entidad.setAdministracionId(dto.getAdministracionId());
        } else {
            entidad = aEntidad(dto);
        }
        return aDto(cursosRepository.save(entidad));
    }

    @Override
    public boolean eliminar(Long id) {
        if (!cursosRepository.existsById(id)) {
            return false;
        }
        cursosRepository.deleteById(id);
        return true;
    }

    private CursoDTO aDto(Cursos entidad) {
        CursoDTO dto = new CursoDTO();
        dto.setId(entidad.getId());
        dto.setMateria(entidad.getMateria());
        dto.setNombreMaestro(entidad.getNombreMaestro());
        dto.setNumSalon(entidad.getNumSalon());
        dto.setCupo(entidad.getCupo());
        dto.setHorario(entidad.getHorario());
        dto.setDocenteEmail(entidad.getDocenteEmail());
        dto.setAdministracionId(entidad.getAdministracionId());
        return dto;
    }

    private Cursos aEntidad(CursoDTO dto) {
        Cursos entidad = new Cursos();
        entidad.setMateria(dto.getMateria());
        entidad.setNombreMaestro(dto.getNombreMaestro());
        entidad.setNumSalon(dto.getNumSalon());
        entidad.setCupo(dto.getCupo());
        entidad.setHorario(dto.getHorario());
        entidad.setDocenteEmail(dto.getDocenteEmail());
        entidad.setAdministracionId(dto.getAdministracionId());
        return entidad;
    }
}
