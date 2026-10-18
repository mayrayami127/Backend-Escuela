package com.app.Cursos.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "inscripciones", uniqueConstraints = @UniqueConstraint(columnNames = "alumno_id"))
public class Inscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "alumno_id", nullable = false)
    private Long alumnoId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", nullable = false)
    private Cursos curso;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getAlumnoId() { return alumnoId; }
    public void setAlumnoId(Long alumnoId) { this.alumnoId = alumnoId; }
    public Cursos getCurso() { return curso; }
    public void setCurso(Cursos curso) { this.curso = curso; }
}
