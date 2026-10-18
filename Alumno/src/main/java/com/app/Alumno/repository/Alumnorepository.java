package com.app.Alumno.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.Alumno.entity.Alumno;

public interface Alumnorepository extends JpaRepository<Alumno, Long> {
}
