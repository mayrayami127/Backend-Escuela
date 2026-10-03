package com.app.Alumno.service;

import java.util.List;

import com.app.Alumno.dto.AlumnoDTO;

public interface AlumnoService {

    List<AlumnoDTO> listar();

    void agregar(AlumnoDTO alumno);

    // true si existía y se eliminó; false si no existe ese id
    boolean eliminar(int id);
}
