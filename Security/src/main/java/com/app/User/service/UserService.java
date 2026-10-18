package com.app.User.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.User.dto.UserDto;
import com.app.User.entity.Role;
import com.app.User.entity.User;
import com.app.User.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean registrarUsuario(UserDto dto) {
        if (userRepo.findByEmail(dto.getEmail()) != null) {
            return false;
        }
        try {
            Role.valueOf(dto.getRole().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Rol inválido. Use ROOT, ADMINISTRATIVO, PRECEPTOR, DOCENTE o DIRECTOR");
        }
        User user = User.getUser(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepo.save(user);
        return true;
    }

    public List<UserDto> listar() {
        return userRepo.findAll().stream().map(this::aDto).collect(Collectors.toList());
    }

    private UserDto aDto(User user) {
        UserDto dto = new UserDto();
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setRole(user.getRole().name());
        return dto;
    }
}
