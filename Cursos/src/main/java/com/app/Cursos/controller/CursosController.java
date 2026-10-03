package com.app.Cursos.controller;

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

import com.app.Cursos.dto.CursoDTO;
import com.app.Cursos.service.CursosService;

@RestController
@RequestMapping("/api/cursos")
public class CursosController {

    @Autowired
    private CursosService cursoService;

    @GetMapping("/cursos")
    public ResponseEntity<List<CursoDTO>> listar() {
        return ResponseEntity.ok(cursoService.listar());
    }

    // Crea (id 0) o actualiza (id existente)
    @PutMapping("/agregar")
    public ResponseEntity<Void> agregar(@RequestBody CursoDTO curso) {
        cursoService.agregar(curso);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        return cursoService.eliminar(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
