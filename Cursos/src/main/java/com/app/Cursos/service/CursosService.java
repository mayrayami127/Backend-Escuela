package com.app.Cursos.service;

import java.util.List;

import com.app.Cursos.dto.CursoDTO;

public interface CursosService {

    List<CursoDTO> listar();

    List<CursoDTO> listarParaUsuario(String email, boolean soloAsignados);

    CursoDTO guardar(CursoDTO curso);

    boolean eliminar(Long id);
}
