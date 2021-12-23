package com.asecurity.web.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.asecurity.domain.Agendamento;
import com.asecurity.domain.Paciente;
import com.asecurity.service.AgendamentoService;
import com.asecurity.service.EspecialidadeService;
import com.asecurity.service.PacienteSevice;

@Controller
@RequestMapping("agendamentos")
public class AgendamentoController {

	@Autowired
	private AgendamentoService agendamentoService;

	@Autowired
	private PacienteSevice pacienteService;

	@Autowired
	private EspecialidadeService especialidadeService;

	@GetMapping("/agendar")
	public String agendar(Agendamento agendamento) {

		return "agendamento/cadastro";

	}

	@GetMapping("/horario/medico/{id}/data/{data}")
	public ResponseEntity<?> getHorarios(@PathVariable("id") Long id, @PathVariable("data") @DateTimeFormat(iso = ISO.DATE) LocalDate data) {

		return ResponseEntity.ok(agendamentoService.buscarHorariosNaoAgendadosPorMedicoIdEData(id, data));
	}

	@PostMapping("/salvar")
	public String salvar(Agendamento agendamento, RedirectAttributes attr, @AuthenticationPrincipal User user) {
		agendamentoService.salvar(agendamento);

		Paciente paciente = pacienteService.buscarPorUsuarioEmail(user.getUsername());

		String titulo = agendamento.getEspecialidade().getTitulo();

//		especialidadeService.

		return "redirect:/agendamento/agendar";
	}

}
