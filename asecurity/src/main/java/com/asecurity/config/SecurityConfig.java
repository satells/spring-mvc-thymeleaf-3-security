package com.asecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import com.asecurity.domain.PerfilTipo;
import com.asecurity.service.UsuarioService;

@EnableGlobalMethodSecurity(prePostEnabled = true)
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

				.antMatchers("/", "/home", "/webjars/**", "/css/**", "/js/**", "/image/**", "/u/novo/cadastro", "/u/cadastro/realizado",
						"/u/cadastro/paciente/salvar", "/u/confirmacao/cadastro", "/u/p/**", "/expired")

				.permitAll()

				// Acessos privados admin
				.antMatchers("/u/editar/senha", "/u/confirmar/senha").hasAnyAuthority(MEDICO, PACIENTE)

				.antMatchers("/u/**").hasAuthority(ADMIN)

				// Acessos privados admin
				.antMatchers("/especialidades/datatables/server/medico/*").hasAnyAuthority(MEDICO, ADMIN)

				.antMatchers("/especialidades/titulo").hasAnyAuthority(MEDICO, ADMIN, PACIENTE)

				.antMatchers("/especialidades/**").hasAuthority(ADMIN)

				// Acessos privados pacientes
				.antMatchers("/pacientes/**").hasAuthority(PACIENTE)

				// Acessos privados m??dicos
				.antMatchers("/medicos/especialidade/titulo/*").hasAnyAuthority(MEDICO, PACIENTE)
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

				.deleteCookies("JSESSIONID")

				// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

				.and()

				.exceptionHandling()

				.accessDeniedPage("/acesso-negado")

				.and()

				.rememberMe();

		http.sessionManagement().maximumSessions(1).expiredUrl("/expired").maxSessionsPreventsLogin(false).sessionRegistry(sessionRegistry());

		http.sessionManagement().sessionFixation().newSession().sessionAuthenticationStrategy(sessionAuthenticationStrategy());

	}

	/**
	 * @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
	 * Para bloquear o primeiro login
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(usuarioService).passwordEncoder(new BCryptPasswordEncoder());
	}

	@Bean
	public SessionRegistry sessionRegistry() {

		return new SessionRegistryImpl();
	}

	@Bean
	public ServletListenerRegistrationBean<?> servletListenerRegistrationBean() {

		return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
	}

	/**
	 * @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
	 * Para invalidar a sess??o do primeiro login
	 */

	@Bean
	public SessionAuthenticationStrategy sessionAuthenticationStrategy() {
		return new RegisterSessionAuthenticationStrategy(sessionRegistry());
	}

}