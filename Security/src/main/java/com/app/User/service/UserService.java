package com.app.User.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.User.dto.UserDto;
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

    // Recibe el DTO; la conversión a entidad ocurre acá, en la capa service
    public boolean registrarUsuario(UserDto dto) {
        if (userRepo.findByEmail(dto.getEmail()) != null) {
            return false;
        }
        User user = User.getUser(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepo.save(user);
        return true;
    }

    public void actualizarUser(User user) {
        if (user.getId() != null && userRepo.existsById(user.getId())) {
            userRepo.save(user);
        }
    }
}
