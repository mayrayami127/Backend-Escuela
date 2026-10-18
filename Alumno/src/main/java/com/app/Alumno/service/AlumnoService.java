package com.app.Alumno.service;

import java.util.List;

import com.app.Alumno.dto.AlumnoDTO;

public interface AlumnoService {

    List<AlumnoDTO> listar();

    AlumnoDTO guardar(AlumnoDTO alumno);

    AlumnoDTO cambiarEstado(Long id, String estado);
}
