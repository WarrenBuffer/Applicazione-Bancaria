package com.tas.applicazionebancaria.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.tas.applicazionebancaria.exceptions.TokenException;

@ControllerAdvice
public class CustomExceptionController {
	@ExceptionHandler(TokenException.class)
	public ModelAndView tokenExcHandler(TokenException exc) {
		System.out.println("Striting: TokenExcHandler");
		return new ModelAndView("redirect:/registrazione");
	}
}
