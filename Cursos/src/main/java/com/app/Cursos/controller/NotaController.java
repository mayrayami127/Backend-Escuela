package com.app.Cursos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.Cursos.dto.NotaDto;
import com.app.Cursos.service.NotaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/notas")
@Tag(name = "Notas API")
public class NotaController {

    @Autowired
    private NotaService notaService;

    @GetMapping("/curso/{cursoId}")
    @PreAuthorize("hasAnyRole('DOCENTE', 'DIRECTOR', 'ROOT', 'PRECEPTOR')")
    @Operation(summary = "Listar notas. El director puede verlas; el docente solo en cursos asignados")
    public ResponseEntity<List<NotaDto>> listarPorCurso(@PathVariable Long cursoId, Authentication authentication) {
        return ResponseEntity.ok(notaService.obtenerNotasPorCurso(cursoId, authentication));
    }

    @PostMapping("/agregar")
    @PreAuthorize("hasRole('DOCENTE')")
    @Operation(summary = "Agregar nota (solo docente del curso asignado)")
    public ResponseEntity<NotaDto> agregarNota(@RequestBody NotaDto dto, Authentication authentication) {
        dto.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(notaService.guardarNota(dto, authentication));
    }

    @PutMapping("/modificar/{id}")
    @PreAuthorize("hasRole('DOCENTE')")
    public ResponseEntity<NotaDto> modificarNota(@PathVariable Long id, @RequestBody NotaDto dto,
                                                 Authentication authentication) {
        dto.setId(id);
        return ResponseEntity.ok(notaService.guardarNota(dto, authentication));
    }

    @DeleteMapping("/eliminar/{id}")
    @PreAuthorize("hasRole('DOCENTE')")
    public ResponseEntity<Void> eliminarNota(@PathVariable Long id, Authentication authentication) {
        notaService.eliminarNota(id, authentication);
        return ResponseEntity.noContent().build();
    }
}
