package com.asecurity.web.conversor;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.asecurity.domain.Especialidade;
import com.asecurity.service.EspecialidadeService;

@Component
public class EspecialidadesConverter implements Converter<String[], Set<Especialidade>> {

	@Autowired
	private EspecialidadeService service;

	@Override
	public Set<Especialidade> convert(String[] titulos) {
		System.out.println("EspecialidadesConverter: ");
		Set<Especialidade> especialidades = new HashSet<Especialidade>();
		if (titulos != null && titulos.length > 0) {
			especialidades.addAll(service.buscaPorTitulos(titulos));

		}

		return especialidades;
	}
}