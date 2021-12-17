package com.asecurity.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asecurity.datatables.Datatables;
import com.asecurity.datatables.DatatablesColunas;
import com.asecurity.domain.Perfil;
import com.asecurity.domain.Usuario;
import com.asecurity.repository.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {
	@Autowired
	private Datatables datatables;

	@Autowired
	private UsuarioRepository repository;

	@Transactional(readOnly = true)
	public Usuario buscarPorEmail(String email) {
		return repository.findByEmail(email);

	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = buscarPorEmail(username);

		return new User(

				usuario.getEmail(),

				usuario.getSenha(),

				AuthorityUtils.createAuthorityList(getAuthorities(usuario.getPerfis()))

		);
	}

	private String[] getAuthorities(List<Perfil> perfis) {

		String[] authorities = new String[perfis.size()];

		for (int i = 0; i < perfis.size(); i++) {
			authorities[i] = perfis.get(i).getDesc();
		}

		return authorities;

	}

	@Transactional(readOnly = true)
	public Map<String, Object> buscarTodos(HttpServletRequest request) {
		datatables.setRequest(request);
		datatables.setColunas(DatatablesColunas.USUARIOS);
		Page<Usuario> page = datatables.getSearch().isEmpty() ? repository.findAll(datatables.getPageable())
				: repository.findPorEmailOrPerfil(datatables.getSearch(), datatables.getPageable());

		return datatables.getResponse(page);
	}

}
