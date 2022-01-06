package com.asecurity.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import com.asecurity.datatables.Datatables;
import com.asecurity.datatables.DatatablesColunas;
import com.asecurity.domain.Perfil;
import com.asecurity.domain.PerfilTipo;
import com.asecurity.domain.Usuario;
import com.asecurity.repository.UsuarioRepository;
import com.asecurity.security.exception.AcessoNegadoException;

@Service
public class UsuarioService implements UserDetailsService {
	@Autowired
	private Datatables datatables;

	@Autowired
	private UsuarioRepository repository;

	@Autowired
	private EmailService emailService;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = buscarPorEmailEAtivo(username).orElseThrow(() -> new UsernameNotFoundException("Usuário " + username + " não encontrado."));

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

	@Transactional(readOnly = false)
	public void salvarUsuario(Usuario usuario) {
		String crypt = new BCryptPasswordEncoder().encode(usuario.getSenha());

		usuario.setSenha(crypt);

		repository.save(usuario);
	}

	@Transactional(readOnly = true)
	public Usuario buscaPorId(Long id) {
		return repository.findById(id).get();
	}

	@Transactional(readOnly = true)
	public Usuario buscaPorIdEPerfis(Long usuarioId, Long[] perfisId) {
		return repository.findByIdAndPerfis(usuarioId, perfisId).orElseThrow(() -> new UsernameNotFoundException("Usuário inexistente."));
	}

	public static boolean isSenhaCorreta(String senhaDigitada, String senhaArmazenada) {
		return new BCryptPasswordEncoder().matches(senhaDigitada, senhaArmazenada);
	}

	@Transactional(readOnly = false)
	public void alterarSenha(Usuario usuario, String senha) {
		usuario.setSenha(new BCryptPasswordEncoder().encode(senha));

		repository.save(usuario);

	}

	@Transactional(readOnly = false)
	public void salvarCadastroPaciente(Usuario usuario) throws MessagingException {
		BCryptPasswordEncoder crypt = new BCryptPasswordEncoder();

		String encoded = crypt.encode(usuario.getSenha());
		usuario.setSenha(encoded);
		usuario.addPerfil(PerfilTipo.PACIENTE);

		repository.save(usuario);

		emailDeConfirmacaoDeCadastro(usuario.getEmail());

	}

	@Transactional(readOnly = true)
	public Usuario buscarPorEmail(String email) {
		return repository.findByEmail(email);

	}

	@Transactional(readOnly = true)
	public Optional<Usuario> buscarPorEmailEAtivo(String email) {

		return repository.findByEmailAndAtivo(email);

	}

	public void emailDeConfirmacaoDeCadastro(String email) throws MessagingException {
		String codigo = Base64Utils.encodeToString(email.getBytes());
		emailService.enviarPedidoDeConformacaoDeCadastro(email, codigo);

	}

	@Transactional(readOnly = false)
	public void ativarCadastroPaciente(String codigo) {
		String email = new String(Base64Utils.decode(codigo.getBytes()));
		Usuario usuario = buscarPorEmail(email);

		if (usuario.hasNotId()) {
			throw new AcessoNegadoException("Não possível ativar seu cadastro.");
		}

		usuario.setAtivo(true);

	}

	@Transactional(readOnly = false)
	public void redefinicaoDeSenha(String email) throws MessagingException {
		Usuario usuario = buscarPorEmailEAtivo(email).orElseThrow(() -> new UsernameNotFoundException("Usuário " + email + " não encontrado."));

		String verificador = RandomStringUtils.randomAlphanumeric(6);

		usuario.setCodigoVerificador(verificador);

		emailService.enviarPedidoDeRedefinicaoDeSenha(email, verificador);

	}

}
