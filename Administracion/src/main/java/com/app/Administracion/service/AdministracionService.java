package com.app.Administracion.service;

import java.util.List;

import com.app.Administracion.dto.AdministracionDTO;

public interface AdministracionService {

    List<AdministracionDTO> listar();

    void agregar(AdministracionDTO administracion);

    // true si existía y se eliminó; false si no existe ese id
    boolean eliminar(int id);
}
