package com.app.Alumno.mappers;

public interface IMapper<E, DTO> {
    DTO toDTO(E entity);
    E toEntity(DTO dto);
}