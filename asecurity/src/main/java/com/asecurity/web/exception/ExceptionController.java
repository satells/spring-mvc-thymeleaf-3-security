package com.asecurity.web.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionController {

	@ExceptionHandler(UsernameNotFoundException.class)
	public ModelAndView usuarioNaoEncontradoException(UsernameNotFoundException e) {

		ModelAndView model = new ModelAndView("error");
		model.addObject("status", "403");
		model.addObject("error", "Operação não pode ser realizada.");
		model.addObject("message", e.getMessage());

		return model;

	}
}
