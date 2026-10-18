package com.app.Administracion.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.Administracion.dto.AdministracionDTO;
import com.app.Administracion.service.AdministracionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/administracion")
@Tag(name = "Personal del colegio")
public class AdministracionController {

    @Autowired
    private AdministracionService administracionService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROOT', 'ADMINISTRATIVO', 'DIRECTOR')")
    @Operation(summary = "Listar docentes, preceptores y administrativos")
    public ResponseEntity<List<AdministracionDTO>> listar() {
        return ResponseEntity.ok(administracionService.listar());
    }

    @PostMapping("/agregar")
    @PreAuthorize("hasAnyRole('ROOT', 'ADMINISTRATIVO')")
    public ResponseEntity<AdministracionDTO> agregar(@RequestBody AdministracionDTO administracion) {
        administracion.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(administracionService.guardar(administracion));
    }

    @PutMapping("/modificar/{id}")
    @PreAuthorize("hasAnyRole('ROOT', 'ADMINISTRATIVO')")
    public ResponseEntity<AdministracionDTO> modificar(@PathVariable Long id,
                                                       @RequestBody AdministracionDTO administracion) {
        administracion.setId(id);
        return ResponseEntity.ok(administracionService.guardar(administracion));
    }

    @DeleteMapping("/eliminar/{id}")
    @PreAuthorize("hasAnyRole('ROOT', 'ADMINISTRATIVO')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        return administracionService.eliminar(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
