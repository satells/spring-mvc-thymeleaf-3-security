package com.asecurity.repository.projection;

import com.asecurity.domain.Especialidade;
import com.asecurity.domain.Medico;
import com.asecurity.domain.Paciente;

public interface HistoricoPaciente {

	Long getId();

	Paciente getPaciente();

	String getDataConsulta();

	Medico getMedico();

	Especialidade getEspecialidade();

}
