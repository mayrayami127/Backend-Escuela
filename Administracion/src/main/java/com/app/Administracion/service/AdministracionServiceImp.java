package com.app.Administracion.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.Administracion.dto.AdministracionDTO;
import com.app.Administracion.entity.Administracion;
import com.app.Administracion.exception.ResourceNotFoundException;
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
    public AdministracionDTO guardar(AdministracionDTO dto) {
        Administracion entidad;
        if (dto.getId() != null) {
            entidad = administracionRepository.findById(dto.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Personal no encontrado: " + dto.getId()));
            entidad.setNombre(dto.getNombre());
            entidad.setApellido(dto.getApellido());
            entidad.setDni(dto.getDni());
            entidad.setEmail(dto.getEmail());
            entidad.setEspecialidad(dto.getEspecialidad());
            entidad.setTipoPersonal(dto.getTipoPersonal());
        } else {
            entidad = aEntidad(dto);
        }
        return aDto(administracionRepository.save(entidad));
    }

    @Override
    public boolean eliminar(Long id) {
        if (!administracionRepository.existsById(id)) {
            return false;
        }
        administracionRepository.deleteById(id);
        return true;
    }

    private AdministracionDTO aDto(Administracion entidad) {
        AdministracionDTO dto = new AdministracionDTO();
        dto.setId(entidad.getId());
        dto.setNombre(entidad.getNombre());
        dto.setApellido(entidad.getApellido());
        dto.setDni(entidad.getDni());
        dto.setEmail(entidad.getEmail());
        dto.setEspecialidad(entidad.getEspecialidad());
        dto.setTipoPersonal(entidad.getTipoPersonal());
        return dto;
    }

    private Administracion aEntidad(AdministracionDTO dto) {
        Administracion entidad = new Administracion();
        entidad.setNombre(dto.getNombre());
        entidad.setApellido(dto.getApellido());
        entidad.setDni(dto.getDni());
        entidad.setEmail(dto.getEmail());
        entidad.setEspecialidad(dto.getEspecialidad());
        entidad.setTipoPersonal(dto.getTipoPersonal());
        return entidad;
    }
}
