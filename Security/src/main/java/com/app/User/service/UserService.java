package com.app.User.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public boolean registrarUsuario(User user) {
        if (userRepo.findByEmail(user.getEmail()) != null) return false;
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        return true;
    }

	public void actualizarUser(User user) {
		if (user.getId() != null && userRepo.existsById(user.getId())) {
			userRepo.save(user);
		}
	}
}