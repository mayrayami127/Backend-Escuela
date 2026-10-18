package com.app.Cursos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.Cursos.entity.Cursos;

public interface CursosRepository extends JpaRepository<Cursos, Long> {
    List<Cursos> findByDocenteEmail(String docenteEmail);
}
