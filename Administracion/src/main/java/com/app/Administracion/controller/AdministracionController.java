package com.app.Administracion.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.Administracion.dto.AdministracionDTO;
import com.app.Administracion.service.AdministracionService;

@RestController
@RequestMapping("/api/administracion")
public class AdministracionController {

    @Autowired
    private AdministracionService administracionService;

    @GetMapping("/Administracion")
    public ResponseEntity<List<AdministracionDTO>> listar() {
        return ResponseEntity.ok(administracionService.listar());
    }

    // Crea (id 0) o actualiza (id existente)
    @PutMapping("/agregar")
    public ResponseEntity<Void> agregar(@RequestBody AdministracionDTO administracion) {
        administracionService.agregar(administracion);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        return administracionService.eliminar(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
