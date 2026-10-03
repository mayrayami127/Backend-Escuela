package com.app.Cursos.dto;

/**
 * DTO: es lo único que viaja entre el cliente y el controller.
 * La entidad JPA queda adentro del microservicio (capa service / repository).
 */
public class CursoDTO {

    private int id;
    private String materia;
    private String nombreMaestro;
    private int numSalon;
    private long administracionId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getNombreMaestro() {
        return nombreMaestro;
    }

    public void setNombreMaestro(String nombreMaestro) {
        this.nombreMaestro = nombreMaestro;
    }

    public int getNumSalon() {
        return numSalon;
    }

    public void setNumSalon(int numSalon) {
        this.numSalon = numSalon;
    }

    public long getAdministracionId() {
        return administracionId;
    }

    public void setAdministracionId(long administracionId) {
        this.administracionId = administracionId;
    }
}
