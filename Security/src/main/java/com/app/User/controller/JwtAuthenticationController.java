package com.app.User.controller;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.User.confing.JwtTokenUtil;
import com.app.User.dto.UserDto;
import com.app.User.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import model.JwtRequest;
import model.JwtResponse;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticación y usuarios")
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @PostMapping("/authenticate")
    @Operation(summary = "Login. Devuelve JWT y roles para el front Angular")
    public ResponseEntity<JwtResponse> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest)
            throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Authorization", "Bearer " + token);

        return new ResponseEntity<>(
                new JwtResponse(token, userDetails.getUsername(), roles),
                responseHeaders,
                HttpStatus.OK);
    }

    private void authenticate(String username, String password) throws Exception {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @PostMapping("/registrar")
    @PreAuthorize("hasRole('ROOT')")
    @Operation(summary = "Root crea usuarios con perfil (ROOT, ADMINISTRATIVO, PRECEPTOR, DOCENTE, DIRECTOR)")
    public ResponseEntity<String> registrar(@Valid @RequestBody UserDto dto) {
        return userService.registrarUsuario(dto)
                ? ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado")
                : ResponseEntity.status(HttpStatus.CONFLICT).body("El email ya existe");
    }

    @GetMapping("/usuarios")
    @PreAuthorize("hasRole('ROOT')")
    public ResponseEntity<List<UserDto>> listarUsuarios() {
        return ResponseEntity.ok(userService.listar());
    }

    @GetMapping("/validate")
    public ResponseEntity<Void> validate(@RequestParam String token) {
        return jwtTokenUtil.isTokenValid(token)
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
