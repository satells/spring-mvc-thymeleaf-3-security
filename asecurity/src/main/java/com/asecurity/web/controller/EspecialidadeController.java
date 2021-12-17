package com.asecurity.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.asecurity.domain.Especialidade;
import com.asecurity.service.EspecialidadeService;

@Controller
@RequestMapping("especialidades")
public class EspecialidadeController {

	@Autowired
	private EspecialidadeService service;

	@GetMapping({ "", "/" })
	public String especialidade(Especialidade especialidade) {
		return "especialidade/especialidade";
	}

	@PostMapping("/salvar")
	public String salvar(Especialidade especialidade, RedirectAttributes attr) {
		service.salvar(especialidade);
		attr.addFlashAttribute("sucesso", "Operação realizada com sucesso.");

		return "redirect:/especialidades";
	}

	@GetMapping("/datatables/server")
	public ResponseEntity<?> getEspecialidades(HttpServletRequest request) {

		return ResponseEntity.ok(service.buscarEspecialidades(request));
	}

	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") Long id, ModelMap model) {

		Especialidade especialidade = service.buscarPorId(id);
		model.addAttribute("especialidade", especialidade);
		return "especialidade/especialidade";

	}

	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id, RedirectAttributes attr) {
		service.excluirPeloId(id);
		attr.addFlashAttribute("sucesso", "Operação realizada com sucesso.");
		return "redirect:/especialidades";

	}

}
