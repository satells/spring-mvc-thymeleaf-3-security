package com.asecurity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asecurity.domain.Usuario;
import com.asecurity.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository repository;

	public Usuario buscarPorEmail(String email) {
		return repository.findByEmail(email);

	}

}
