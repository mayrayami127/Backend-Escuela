package com.app.Cursos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.Cursos.entity.Nota;

public interface NotaRepository extends JpaRepository<Nota, Long> {
    List<Nota> findByCurso_Id(Long cursoId);
}
