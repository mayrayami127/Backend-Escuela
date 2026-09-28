package com.app.User.service;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// Usuario de prueba predeterminado
		if ("javainuse".equals(username) || "admin".equals(username)) {
			return new org.springframework.security.core.userdetails.User(
					username, 
					"$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6", // "password" en BCrypt
					new ArrayList<>()
			);
		} else {
			throw new UsernameNotFoundException("Usuario no encontrado con el nombre: " + username);
		}
	}
}