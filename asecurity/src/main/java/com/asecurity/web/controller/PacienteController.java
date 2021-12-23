package com.asecurity.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.asecurity.domain.Paciente;
import com.asecurity.domain.Usuario;
import com.asecurity.service.PacienteSevice;
import com.asecurity.service.UsuarioService;

@Controller
@RequestMapping("pacientes")
public class PacienteController {

	@Autowired
	private PacienteSevice pacienteService;

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping("/dados")
	public String cadastro(Paciente paciente, ModelMap model, @AuthenticationPrincipal User user) {

		paciente = pacienteService.buscarPorUsuarioEmail(user.getUsername());
		if (paciente.hasNotId()) {
			paciente.setUsuario(new Usuario(user.getUsername()));
		}
		model.addAttribute("paciente", paciente);

		return "paciente/cadastro";
	}

	@PostMapping("/salvar")
	public String salvar(Paciente paciente, ModelMap model, @AuthenticationPrincipal User user) {

		Usuario u = usuarioService.buscarPorEmail(user.getUsername());

		if (UsuarioService.isSenhaCorreta(paciente.getUsuario().getSenha(), u.getSenha())) {
			paciente.setUsuario(u);

			pacienteService.salvar(paciente);
			model.addAttribute("sucesso", "Seus dados foram inseridos.");
		} else {
			model.addAttribute("falha", "Sua senha não confere.");
		}

		return "paciente/cadastro";
	}

	@PostMapping("/editar")
	public String editar(Paciente paciente, ModelMap model, @AuthenticationPrincipal User user) {
		Usuario usuario = usuarioService.buscarPorEmail(paciente.getUsuario().getEmail());

		if (UsuarioService.isSenhaCorreta(paciente.getUsuario().getSenha(), usuario.getSenha())) {

			pacienteService.editar(paciente);

			model.addAttribute("sucesso", "Dados alterados com sucesso");

		} else {
			model.addAttribute("falha", "Senha não confere");
		}

		return "paciente/cadastro";
	}
}
