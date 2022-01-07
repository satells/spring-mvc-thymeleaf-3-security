package com.asecurity.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	// abrir pagina home
	@GetMapping({ "/", "/home" })
	public String home() {
		return "home";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/login-error")
	public String loginError(ModelMap model, HttpServletRequest request) {

		HttpSession session = request.getSession();

		String lastException = String.valueOf(session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION"));

		if (lastException.contains(SessionAuthenticationException.class.getName())) {
			model.addAttribute("alerta", "erro");
			model.addAttribute("titulo", "Acesso recusado.");
			model.addAttribute("texto", "Você já está logado em outro dispositivo.");
			model.addAttribute("subtexto", "Faça logout ou espere sua sessão expirar.");

			return "login";
		}

		model.addAttribute("alerta", "erro");
		model.addAttribute("titulo", "Credenciais inválidas.");
		model.addAttribute("texto", "Login ou senha incorretos.");
		model.addAttribute("subtexto", "Acesso permitido apenas para cadastros já ativados.");

		return "login";

	}

	@GetMapping("/expired")
	public String sessaoExpirada(ModelMap model) {
		model.addAttribute("alerta", "erro");
		model.addAttribute("titulo", "Acesso recusado.");
		model.addAttribute("texto", "Sua sessão expirou.");
		model.addAttribute("subtexto", "Você logou em outro dispositivo.");

		return "login";

	}

	@GetMapping("/acesso-negado")
	public String acessoNegado(ModelMap model, HttpServletResponse response) {

		model.addAttribute("status", response.getStatus());
		model.addAttribute("error", "Acesso negado.");
		model.addAttribute("message", "Você não tem permissão para esta área ou ação.");

		return "error";

	}

}
