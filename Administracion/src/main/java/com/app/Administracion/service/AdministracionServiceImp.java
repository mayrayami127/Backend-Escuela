package com.app.Administracion.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.Administracion.dto.AdministracionDTO;
import com.app.Administracion.entity.Administracion;
import com.app.Administracion.repository.AdministracionRepository;

@Service
public class AdministracionServiceImp implements AdministracionService {

    @Autowired
    private AdministracionRepository administracionRepository;

    @Override
    public List<AdministracionDTO> listar() {
        return administracionRepository.findAll().stream()
                .map(this::aDto)
                .collect(Collectors.toList());
    }

    @Override
    public void agregar(AdministracionDTO dto) {
        administracionRepository.save(aEntidad(dto));
    }

    @Override
    public boolean eliminar(int id) {
        if (!administracionRepository.existsById(id)) {
            return false;
        }
        administracionRepository.deleteById(id);
        return true;
    }

    // ---- Mapeo entidad <-> DTO ----
    private AdministracionDTO aDto(Administracion entidad) {
        AdministracionDTO dto = new AdministracionDTO();
        dto.setId(entidad.getId());
        dto.setNombre(entidad.getNombre());
        dto.setApellido(entidad.getApellido());
        dto.setDni(entidad.getDni());
        dto.setEmail(entidad.getEmail());
        dto.setEspecialidad(entidad.getEspecialidad());
        return dto;
    }

    private Administracion aEntidad(AdministracionDTO dto) {
        Administracion entidad = new Administracion();
        entidad.setId(dto.getId());
        entidad.setNombre(dto.getNombre());
        entidad.setApellido(dto.getApellido());
        entidad.setDni(dto.getDni());
        entidad.setEmail(dto.getEmail());
        entidad.setEspecialidad(dto.getEspecialidad());
        return entidad;
    }
}
