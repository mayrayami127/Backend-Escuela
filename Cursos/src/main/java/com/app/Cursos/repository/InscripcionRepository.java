package com.app.Cursos.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.app.Cursos.entity.Inscripcion;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
    Optional<Inscripcion> findByAlumnoId(Long alumnoId);
    long countByCurso_Id(Long cursoId);
    List<Inscripcion> findByCurso_Id(Long cursoId);
}
