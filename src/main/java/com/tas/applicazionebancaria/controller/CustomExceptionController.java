package com.tas.applicazionebancaria.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.tas.applicazionebancaria.exceptions.AdminTokenException;
import com.tas.applicazionebancaria.exceptions.TokenException;
import com.tas.applicazionebancaria.utils.ServerResponse;

@ControllerAdvice
public class CustomExceptionController {
	@ExceptionHandler(TokenException.class)
	public ModelAndView tokenExcHandler(TokenException exc) {
		System.out.println("Striting: TokenExcHandler");
		return new ModelAndView("redirect:/registrazione");
	}
	@ExceptionHandler(AdminTokenException.class)
	public ServerResponse adminTokenExcHandler(TokenException exc) {
		System.out.println("Striting: AdminTokenExcHandler");
		return new ServerResponse(-1, "JWT invalido. Rieffettua il login.");
	}
	
}
