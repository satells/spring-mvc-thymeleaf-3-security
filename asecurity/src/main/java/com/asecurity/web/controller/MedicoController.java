package com.asecurity.web.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.asecurity.domain.Medico;
import com.asecurity.domain.Usuario;
import com.asecurity.service.MedicoService;
import com.asecurity.service.UsuarioService;

@Controller
@RequestMapping("medicos")
public class MedicoController {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private MedicoService medicoService;

	@GetMapping({ "/dados" })
	public String abrirPorMedico(Medico medico, ModelMap model, @AuthenticationPrincipal User user) {

		if (medico.hasNotId()) {
			medico = medicoService.buscarPorEmail(user.getUsername());
			model.addAttribute("medico", medico);
		}

		return "medico/cadastro";
	}

	@PostMapping("/salvar")
	public String salvar(Medico medico, RedirectAttributes attr, @AuthenticationPrincipal User user, Principal principal,
			Authentication authentication) {

		if (medico.hasNotId() && medico.getUsuario().hasNotId()) {

			Usuario usuario = usuarioService.buscarPorEmail(user.getUsername());

			medico.setUsuario(usuario);
		}

		System.out.println(user.getUsername());
		System.out.println(principal.getName());
		System.out.println(authentication.getName());

		medicoService.salvar(medico);

		attr.addFlashAttribute("sucesso", "Médico incluido.");
		attr.addFlashAttribute("medico", medico);

		return "redirect:/medicos/dados";
	}

	@PostMapping("/editar")
	public String editar(Medico medico, RedirectAttributes attr) {

		medicoService.editar(medico);

		attr.addFlashAttribute("sucesso", "Médico alterado.");
		attr.addFlashAttribute("medico", medico);

		return "redirect:/medicos/dados";
	}

	@GetMapping("/id/{idMed}/excluir/especializacao/{idEsp}")
	public String excluirEspecialidadePorMedico(@PathVariable("idMed") Long idMed, @PathVariable("idEsp") Long idEsp, RedirectAttributes attr) {

		medicoService.excluirEspecialidadePorMedico(idMed, idEsp);

		attr.addFlashAttribute("sucesso", "Especialidade removida com sucesso.");

		return "redirect:/medicos/dados";
	}

}