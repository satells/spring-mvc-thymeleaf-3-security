package com.asecurity.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.asecurity.domain.Medico;
import com.asecurity.service.MedicoService;

@Controller
@RequestMapping("medicos")
public class MedicoController {

	@Autowired
	private MedicoService service;

	@GetMapping({ "/dados" })
	public String abrirPorMedico(Medico medico, ModelMap model) {

		return "medico/cadastro";
	}

	@PostMapping("/salvar")
	public String salvar(Medico medico, RedirectAttributes attr) {

		service.salvar(medico);

		attr.addFlashAttribute("sucesso", "Médico incluido.");
		attr.addFlashAttribute("medico", medico);

		return "redirect:/medicos/dados";
	}

	@PostMapping("/editar")
	public String editar(Medico medico, RedirectAttributes attr) {

		service.editar(medico);

		attr.addFlashAttribute("sucesso", "Médico alterado.");
		attr.addFlashAttribute("medico", medico);

		return "redirect:/medicos/dados";
	}
}