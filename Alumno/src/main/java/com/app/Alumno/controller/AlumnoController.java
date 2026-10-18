package com.app.Alumno.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.Alumno.dto.AlumnoDTO;
import com.app.Alumno.service.AlumnoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/alumnos")
@Tag(name = "Alumnos API", description = "Gestión de alumnos. Nunca se eliminan: se deshabilitan o quedan sin asignar")
public class AlumnoController {

    @Autowired
    private AlumnoService alumnoService;

    @GetMapping
    @PreAuthorize("hasAnyRole('PRECEPTOR', 'DIRECTOR', 'ADMINISTRATIVO')")
    @Operation(summary = "Listar alumnos. Root no gestiona alumnos")
    public ResponseEntity<List<AlumnoDTO>> listar() {
        return ResponseEntity.ok(alumnoService.listar());
    }

    @PostMapping("/agregar")
    @PreAuthorize("hasRole('PRECEPTOR')")
    @Operation(summary = "Crear alumno (solo Preceptor; Root no puede agregar alumnos)")
    public ResponseEntity<AlumnoDTO> agregar(@RequestBody AlumnoDTO dto) {
        dto.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(alumnoService.guardar(dto));
    }

    @PutMapping("/modificar/{id}")
    @PreAuthorize("hasRole('PRECEPTOR')")
    public ResponseEntity<AlumnoDTO> modificar(@PathVariable Long id, @RequestBody AlumnoDTO dto) {
        dto.setId(id);
        return ResponseEntity.ok(alumnoService.guardar(dto));
    }

    @PatchMapping("/deshabilitar/{id}")
    @PreAuthorize("hasRole('PRECEPTOR')")
    @Operation(summary = "Deshabilitar alumno sin borrarlo de la base")
    public ResponseEntity<AlumnoDTO> deshabilitar(@PathVariable Long id) {
        return ResponseEntity.ok(alumnoService.cambiarEstado(id, "NOHABILITADO"));
    }
}
