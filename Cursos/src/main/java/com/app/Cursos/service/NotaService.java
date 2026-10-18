package com.app.Cursos.service;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.app.Cursos.dto.NotaDto;

public interface NotaService {
    List<NotaDto> obtenerNotasPorCurso(Long cursoId, Authentication authentication);

    NotaDto guardarNota(NotaDto dto, Authentication authentication);

    void eliminarNota(Long id, Authentication authentication);
}
