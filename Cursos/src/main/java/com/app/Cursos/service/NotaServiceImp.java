package com.app.Cursos.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.app.Cursos.dto.NotaDto;
import com.app.Cursos.entity.Cursos;
import com.app.Cursos.entity.Nota;
import com.app.Cursos.exception.ForbiddenOperationException;
import com.app.Cursos.exception.ResourceNotFoundException;
import com.app.Cursos.repository.CursosRepository;
import com.app.Cursos.repository.NotaRepository;

@Service
public class NotaServiceImp implements NotaService {

    @Autowired
    private NotaRepository notaRepository;

    @Autowired
    private CursosRepository cursosRepository;

    @Override
    public List<NotaDto> obtenerNotasPorCurso(Long cursoId, Authentication authentication) {
        Cursos curso = cursosRepository.findById(cursoId)
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado: " + cursoId));
        validarLectura(curso, authentication);
        return notaRepository.findByCurso_Id(cursoId).stream().map(this::aDto).collect(Collectors.toList());
    }

    @Override
    public NotaDto guardarNota(NotaDto dto, Authentication authentication) {
        Cursos curso = cursosRepository.findById(dto.getCursoId())
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado: " + dto.getCursoId()));
        validarEscrituraDocente(curso, authentication);
        Nota nota;
        if (dto.getId() != null) {
            nota = notaRepository.findById(dto.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Nota no encontrada: " + dto.getId()));
            nota.setAlumnoId(dto.getAlumnoId());
            nota.setCurso(curso);
            nota.setCalificacion(dto.getCalificacion());
            nota.setTipoEvaluacion(dto.getTipoEvaluacion());
        } else {
            nota = aEntidad(dto, curso);
        }
        return aDto(notaRepository.save(nota));
    }

    @Override
    public void eliminarNota(Long id, Authentication authentication) {
        Nota nota = notaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nota no encontrada: " + id));
        validarEscrituraDocente(nota.getCurso(), authentication);
        notaRepository.delete(nota);
    }

    private void validarLectura(Cursos curso, Authentication authentication) {
        if (esSoloDocente(authentication) && !esCursoAsignado(curso, authentication.getName())) {
            throw new ForbiddenOperationException("El docente solo puede ver notas de cursos asignados");
        }
    }

    private void validarEscrituraDocente(Cursos curso, Authentication authentication) {
        if (!esSoloDocente(authentication)) {
            throw new ForbiddenOperationException("Solo el docente puede agregar, modificar o eliminar notas");
        }
        if (!esCursoAsignado(curso, authentication.getName())) {
            throw new ForbiddenOperationException("El docente solo puede gestionar notas de cursos asignados");
        }
    }

    private boolean esSoloDocente(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals("ROLE_DOCENTE"))
                && authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .noneMatch(a -> a.equals("ROLE_DIRECTOR") || a.equals("ROLE_ROOT") || a.equals("ROLE_PRECEPTOR"));
    }

    private boolean esCursoAsignado(Cursos curso, String email) {
        return curso.getDocenteEmail() != null && curso.getDocenteEmail().equalsIgnoreCase(email);
    }

    private NotaDto aDto(Nota nota) {
        NotaDto dto = new NotaDto();
        dto.setId(nota.getId());
        dto.setAlumnoId(nota.getAlumnoId());
        dto.setCursoId(nota.getCursoId());
        dto.setCalificacion(nota.getCalificacion());
        dto.setTipoEvaluacion(nota.getTipoEvaluacion());
        return dto;
    }

    private Nota aEntidad(NotaDto dto, Cursos curso) {
        Nota nota = new Nota();
        nota.setAlumnoId(dto.getAlumnoId());
        nota.setCurso(curso);
        nota.setCalificacion(dto.getCalificacion());
        nota.setTipoEvaluacion(dto.getTipoEvaluacion());
        return nota;
    }
}
