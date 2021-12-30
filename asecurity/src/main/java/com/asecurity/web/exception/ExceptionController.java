package com.asecurity.web.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.asecurity.security.exception.AcessoNegadoException;

@ControllerAdvice
public class ExceptionController {

	@ExceptionHandler(UsernameNotFoundException.class)
	public ModelAndView usuarioNaoEncontradoException(UsernameNotFoundException e) {

		ModelAndView model = new ModelAndView("error");
		model.addObject("status", "404");
		model.addObject("error", "Operação não pode ser realizada.");
		model.addObject("message", e.getMessage());

		return model;

	}

	@ExceptionHandler(AcessoNegadoException.class)
	public ModelAndView usuarioNaoEncontradoException(AcessoNegadoException e) {

		ModelAndView model = new ModelAndView("error");
		model.addObject("status", "403");
		model.addObject("error", "Operação não pode ser realizada.");
		model.addObject("message", e.getMessage());

		return model;

	}
}
