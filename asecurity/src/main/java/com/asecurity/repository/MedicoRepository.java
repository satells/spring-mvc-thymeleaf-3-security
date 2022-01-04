package com.asecurity.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.asecurity.domain.Medico;

public interface MedicoRepository extends JpaRepository<Medico, Long> {

	@Query("select m from Medico m where m.usuario.id = :id")
	Optional<Medico> findByUsuarioId(Long id);

	@Query("select m from Medico m where m.usuario.email = :email")
	Optional<Medico> findByUsuarioEmail(String email);

	@Query("select distinct m from Medico m join m.especialidades e where e.titulo like :titulo and m.usuario.ativo = true order by m.nome asc")
	List<Medico> findByMedicosPorEspecialidade(String titulo);

	@Query("select m.id from Medico m join m.especialidades e join m.agendamentos a where a.especialidade.id = :idEsp and a.medico.id = :idMed ")
	Optional<Long> hasEspecialidadeAgendada(Long idMed, Long idEsp);

}
