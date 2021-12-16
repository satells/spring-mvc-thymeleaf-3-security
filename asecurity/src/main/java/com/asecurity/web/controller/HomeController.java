package com.asecurity.web.controller;

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
	public String loginError(ModelMap model) {

		model.addAttribute("alerta", "erro");
		model.addAttribute("titulo", "Credenciais inválidas.");
		model.addAttribute("texto", "Login ou senha incorretos.");
		model.addAttribute("subtexto", "Acesso permitido apenas para cadastros já ativados.");

		return "login";

	}

}
