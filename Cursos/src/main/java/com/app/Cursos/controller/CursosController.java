package com.app.Cursos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.Cursos.dto.CursoDTO;
import com.app.Cursos.service.CursosService;
import com.app.Cursos.service.InscripcionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/cursos")
@Tag(name = "Cursos API")
public class CursosController {

	@Autowired
	private InscripcionService inscripcionService;

	@GetMapping("/{id}/inscripciones")
	@PreAuthorize("hasAnyRole('ROOT', 'PRECEPTOR', 'DIRECTOR')")
	@Operation(summary = "IDs de alumnos inscriptos en el curso (sirve para probar RabbitMQ)")
	public ResponseEntity<List<Long>> inscripciones(@PathVariable Long id) {
	    return ResponseEntity.ok(inscripcionService.alumnosDelCurso(id));
	}
	
    @Autowired
    private CursosService cursoService;

    @GetMapping
    @PreAuthorize("hasAnyRole('DOCENTE', 'DIRECTOR', 'ROOT', 'PRECEPTOR', 'ADMINISTRATIVO')")
    @Operation(summary = "Listar cursos. El docente solo ve los asignados; el director ve todos")
    public ResponseEntity<List<CursoDTO>> listar(Authentication authentication) {
        boolean soloAsignados = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals("ROLE_DOCENTE"))
                && authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .noneMatch(a -> a.equals("ROLE_DIRECTOR") || a.equals("ROLE_ROOT") || a.equals("ROLE_PRECEPTOR"));
        return ResponseEntity.ok(cursoService.listarParaUsuario(authentication.getName(), soloAsignados));
    }

    @PostMapping("/agregar")
    @PreAuthorize("hasAnyRole('ROOT', 'PRECEPTOR')")
    @Operation(summary = "Crear curso (Root o Preceptor)")
    public ResponseEntity<CursoDTO> agregar(@RequestBody CursoDTO curso) {
        curso.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.guardar(curso));
    }

    @PutMapping("/modificar/{id}")
    @PreAuthorize("hasAnyRole('ROOT', 'PRECEPTOR')")
    public ResponseEntity<CursoDTO> modificar(@PathVariable Long id, @RequestBody CursoDTO curso) {
        curso.setId(id);
        return ResponseEntity.ok(cursoService.guardar(curso));
    }

    @DeleteMapping("/eliminar/{id}")
    @PreAuthorize("hasAnyRole('ROOT', 'PRECEPTOR')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        return cursoService.eliminar(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
