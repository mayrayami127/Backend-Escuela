package com.app.User.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.app.User.entity.Role;
import com.app.User.entity.User;
import com.app.User.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            crearSiNoExiste(userRepository, passwordEncoder, "root@escuela.com", "root123", "Root", "Admin", Role.ROOT);
            crearSiNoExiste(userRepository, passwordEncoder, "preceptor@escuela.com", "prec123", "Ana", "Preceptor", Role.PRECEPTOR);
            crearSiNoExiste(userRepository, passwordEncoder, "docente@escuela.com", "doc123", "Luis", "Docente", Role.DOCENTE);
            crearSiNoExiste(userRepository, passwordEncoder, "director@escuela.com", "dir123", "Marta", "Director", Role.DIRECTOR);
            crearSiNoExiste(userRepository, passwordEncoder, "admin@escuela.com", "adm123", "Pablo", "Administrativo", Role.ADMINISTRATIVO);
        };
    }

    private void crearSiNoExiste(UserRepository repo, PasswordEncoder encoder, String email, String password,
                                 String nombre, String apellido, Role role) {
        if (repo.findByEmail(email) == null) {
            User user = new User();
            user.setEmail(email);
            user.setPassword(encoder.encode(password));
            user.setFirstName(nombre);
            user.setLastName(apellido);
            user.setRole(role);
            repo.save(user);
        }
    }
}
