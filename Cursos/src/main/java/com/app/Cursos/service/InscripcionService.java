package com.app.Cursos.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.Cursos.dto.AlumnoEventoDto;
import com.app.Cursos.entity.Cursos;
import com.app.Cursos.entity.Inscripcion;
import com.app.Cursos.repository.CursosRepository;
import com.app.Cursos.repository.InscripcionRepository;

@Service
public class InscripcionService {

    private static final Logger log = LoggerFactory.getLogger(InscripcionService.class);

    @Autowired
    private InscripcionRepository inscripcionRepository;

    @Autowired
    private CursosRepository cursosRepository;

    @Transactional
    public void procesarEventoAlumno(AlumnoEventoDto e) {
        boolean habilitado = "HABILITADO".equalsIgnoreCase(e.getEstado());

        // Deshabilitado o sin curso: libera el lugar
        if (!habilitado || e.getCursoId() == null) {
            inscripcionRepository.findByAlumnoId(e.getId()).ifPresent(i -> {
                inscripcionRepository.delete(i);
                log.info("Alumno {} liberado del curso {}", e.getId(), i.getCurso().getId());
            });
            return;
        }

        Optional<Cursos> cursoOpt = cursosRepository.findById(e.getCursoId());
        if (cursoOpt.isEmpty()) {
            log.warn("Alumno {}: el curso {} no existe, no se inscribe", e.getId(), e.getCursoId());
            return;
        }
        Cursos curso = cursoOpt.get();

        Optional<Inscripcion> actual = inscripcionRepository.findByAlumnoId(e.getId());
        if (actual.isPresent() && actual.get().getCurso().getId().equals(curso.getId())) {
            return; // ya estaba inscripto en ese curso
        }
        if (inscripcionRepository.countByCurso_Id(curso.getId()) >= curso.getCupo()) {
            log.warn("Cupo lleno en el curso {} (cupo {}): alumno {} NO inscripto",
                    curso.getId(), curso.getCupo(), e.getId());
            return;
        }

        Inscripcion i = actual.orElseGet(Inscripcion::new);
        i.setAlumnoId(e.getId());
        i.setCurso(curso);
        inscripcionRepository.save(i);
        log.info("Alumno {} inscripto en el curso {}", e.getId(), curso.getId());
    }

    @Transactional(readOnly = true)
    public List<Long> alumnosDelCurso(Long cursoId) {
        return inscripcionRepository.findByCurso_Id(cursoId).stream()
                .map(Inscripcion::getAlumnoId).collect(Collectors.toList());
    }
}
