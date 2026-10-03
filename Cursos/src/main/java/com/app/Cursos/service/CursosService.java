package com.app.Cursos.service;

import java.util.List;

import com.app.Cursos.dto.CursoDTO;

public interface CursosService {

    List<CursoDTO> listar();

    void agregar(CursoDTO curso);

    // true si existía y se eliminó; false si no existe ese id
    boolean eliminar(int id);
}
