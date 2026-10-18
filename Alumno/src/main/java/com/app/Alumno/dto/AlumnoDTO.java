package com.app.Alumno.dto;

import com.app.Alumno.Enum.Estado;

public class AlumnoDTO {
    private Long id; // Solo para lectura/respuestas REST, no requerido en creación
    private String nombre;
    private String apellido;
    private String gmail;
    private Long cursoId;
    private Estado estado;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getGmail() { return gmail; }
    public void setGmail(String gmail) { this.gmail = gmail; }
    public Long getCursoId() { return cursoId; }
    public void setCursoId(Long cursoId) { this.cursoId = cursoId; }
    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }
}
