package com.app.Alumno.dto;

/**
 * DTO: es lo único que viaja entre el cliente y el controller.
 * La entidad JPA queda adentro del microservicio (capa service / repository).
 */
public class AlumnoDTO {

    private int id;
    private String nombre;
    private String apellido;
    private String gmail;
    private Long cursoId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public Long getCursoId() {
        return cursoId;
    }

    public void setCursoId(Long cursoId) {
        this.cursoId = cursoId;
    }
}
