package com.tas.applicazionebancaria.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.ModelAndView;

import com.tas.applicazionebancaria.ApplicazioneBancariaApplication;
import com.tas.applicazionebancaria.exceptions.AdminTokenException;
import com.tas.applicazionebancaria.exceptions.TokenException;
import com.tas.applicazionebancaria.utils.ServerResponse;

@SpringBootTest(classes = ApplicazioneBancariaApplication.class)
class CustomExceptionTest {
	private CustomExceptionController customExceptionController=new CustomExceptionController();
	@Test
	void tokenExcHandlerTest() {
		ModelAndView mv=customExceptionController.tokenExcHandler(new TokenException("Messaggio"));
		assertEquals("redirect:/registrazione",mv.getViewName());
	}
	@Test
	void adminTokenExcHandlerTest() {
		ServerResponse sr=customExceptionController.adminTokenExcHandler(new AdminTokenException("Messaggio"));
		assertEquals(sr.getCode(), -1);
		assertEquals(sr.getMessage(), "JWT invalido. Rieffettua il login.");
	}

}
