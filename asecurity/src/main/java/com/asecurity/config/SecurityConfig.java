package com.asecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.asecurity.domain.PerfilTipo;
import com.asecurity.service.UsuarioService;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String ADMIN = PerfilTipo.ADMIN.getDesc();
	private static final String MEDICO = PerfilTipo.MEDICO.getDesc();
	private static final String PACIENTE = PerfilTipo.PACIENTE.getDesc();

	@Autowired
	private UsuarioService usuarioService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

		http.authorizeRequests()

				.antMatchers("/", "/home", "/webjars/**", "/css/**", "/js/**", "/image/**")

				.permitAll()

				// Acessos privados admin
				.antMatchers("/u/editar/senha", "/u/confirmar/senha").hasAuthority(MEDICO)

				.antMatchers("/u/**").hasAuthority(ADMIN)

				// Acessos privados admin
				.antMatchers("/especialidades/datatables/server/medico/*").hasAnyAuthority(MEDICO, ADMIN)

				.antMatchers("/especialidades/titulo").hasAnyAuthority(MEDICO, ADMIN)

				.antMatchers("/especialidades/**").hasAuthority(ADMIN)

				// Acessos privados pacientes
				.antMatchers("/pacientes/**").hasAuthority(PACIENTE)

				// Acessos privados médicos
				.antMatchers("/medicos/dados", "/medicos/salvar", "/medicos/editar").hasAnyAuthority(MEDICO, ADMIN)

				.antMatchers("/medicos/**").hasAuthority(MEDICO)

				.anyRequest()

				.authenticated()

				.and()

				// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
				.formLogin()

				.loginPage("/login")

				.defaultSuccessUrl("/", true)

				.failureUrl("/login-error")

				.permitAll()

				// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

				.and()

				.logout()

				.logoutSuccessUrl("/")

				// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

				.and()

				.exceptionHandling()

				.accessDeniedPage("/acesso-negado");

	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(usuarioService).passwordEncoder(new BCryptPasswordEncoder());
	}
}