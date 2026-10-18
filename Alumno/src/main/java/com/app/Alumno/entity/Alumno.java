package com.app.Alumno.entity;

import com.app.Alumno.Enum.Estado;
import jakarta.persistence.*;

@Entity
@Table(name = "alumnos")
public class Alumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false, unique = true)
    private String gmail;

    @Column(name = "curso_id")
    private Long cursoId;

    // HABILITADO, NOHABILITADO, SIN_ASIGNAR (los alumnos nunca se borran)
    @Enumerated(EnumType.STRING)
    private Estado estado = Estado.HABILITADO;

    public Alumno() {
    }

    public Alumno(Long id, String nombre, String apellido, String gmail, Long cursoId, Estado estado) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.gmail = gmail;
        this.cursoId = cursoId;
        this.estado = estado;
    }

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
