package com.asecurity.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asecurity.domain.Agendamento;
import com.asecurity.domain.Horario;
import com.asecurity.repository.AgendamentoRepository;

@Service
public class AgendamentoService {

	@Autowired
	private AgendamentoRepository repository;

	public void salvar(Agendamento agendamento) {
		repository.save(agendamento);
	}

	@Transactional(readOnly = true)
	public List<Horario> buscarHorariosNaoAgendadosPorMedicoIdEData(Long id, LocalDate data) {
		return repository.findByMedicoIdAndDataNotHorarioAgendado(id, data);
	}

}
