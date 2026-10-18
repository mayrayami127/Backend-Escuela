package com.app.Alumno.dto;

import com.app.Alumno.Enum.Estado;

public class AlumnoEstadoDto {
    private Long id;
    private Estado estado;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }
}
