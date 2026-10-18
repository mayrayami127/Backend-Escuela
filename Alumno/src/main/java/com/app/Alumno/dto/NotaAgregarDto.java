package com.app.Alumno.dto;

import com.app.Alumno.Enum.TipoEvaluacion;

public class NotaAgregarDto {
    private Long alumnoId;
    private Long cursoId;
    private Double calificacion;
    private TipoEvaluacion tipoEvaluacion;

    public Long getAlumnoId() { return alumnoId; }
    public void setAlumnoId(Long alumnoId) { this.alumnoId = alumnoId; }
    public Long getCursoId() { return cursoId; }
    public void setCursoId(Long cursoId) { this.cursoId = cursoId; }
    public Double getCalificacion() { return calificacion; }
    public void setCalificacion(Double calificacion) { this.calificacion = calificacion; }
    public TipoEvaluacion getTipoEvaluacion() { return tipoEvaluacion; }
    public void setTipoEvaluacion(TipoEvaluacion tipoEvaluacion) { this.tipoEvaluacion = tipoEvaluacion; }
}
