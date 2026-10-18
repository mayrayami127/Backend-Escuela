package com.app.Administracion.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.Administracion.entity.Administracion;

public interface AdministracionRepository extends JpaRepository<Administracion, Long> {
}
