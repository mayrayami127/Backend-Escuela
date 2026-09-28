package com.app.User.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.User.entity.User;
import com.app.User.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepo;

	@Autowired
	public UserService(UserRepository userRepo) {
		this.userRepo = userRepo;
	}

	public void registrarUsuario(User user) {
		userRepo.save(user);
	}

	public void actualizarUser(User user) {
		if (user.getId() != null && userRepo.existsById(user.getId())) {
			userRepo.save(user);
		}
	}
}