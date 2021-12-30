package com.asecurity.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.asecurity.domain.Agendamento;
import com.asecurity.domain.Horario;
import com.asecurity.repository.projection.HistoricoPaciente;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

	@Query("select h from Horario h where not exists(select a.horario.id from Agendamento a where a.medico.id = :id and a.dataConsulta = :data and a.horario.id = h.id) order by h.horaMinuto asc")
	List<Horario> findByMedicoIdAndDataNotHorarioAgendado(Long id, LocalDate data);

	@Query("select "

			+ "a.id as id,"

			+ "a.paciente as paciente, "

			+ "concat(function('TO_CHAR',a.dataConsulta,'DD/MM/YYYY'), ' - ', function('TO_CHAR', a.horario.horaMinuto,'HH:MI')) as dataConsulta,"

			+ "a.medico as medico,"

			+ "a.especialidade as especialidade"

			+ " from Agendamento a "

			+ "where a.paciente.usuario.email like :email")
	Page<HistoricoPaciente> findHistoricoByPacienteEmail(String email, Pageable pageable);

	@Query("select "

			+ "a.id as id,"

			+ "a.paciente as paciente, "

			+ "concat(function('TO_CHAR',a.dataConsulta,'DD/MM/YYYY'), ' - ', function('TO_CHAR', a.horario.horaMinuto,'HH:MI')) as dataConsulta,"

			+ "a.medico as medico,"

			+ "a.especialidade as especialidade"

			+ " from Agendamento a "

			+ "where a.medico.usuario.email like :email")
	Page<HistoricoPaciente> findHistoricoByMedicoEmail(String email, Pageable pageable);

}
