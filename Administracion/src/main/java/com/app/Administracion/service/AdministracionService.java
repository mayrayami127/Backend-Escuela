package com.app.Administracion.service;

import java.util.List;

import com.app.Administracion.dto.AdministracionDTO;

public interface AdministracionService {

    List<AdministracionDTO> listar();

    AdministracionDTO guardar(AdministracionDTO administracion);

    boolean eliminar(Long id);
}
