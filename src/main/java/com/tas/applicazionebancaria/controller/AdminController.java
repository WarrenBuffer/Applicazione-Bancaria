package com.tas.applicazionebancaria.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tas.applicazionebancaria.businesscomponent.model.Amministratore;
import com.tas.applicazionebancaria.businesscomponent.model.Cliente;
import com.tas.applicazionebancaria.service.AmministratoreService;
import com.tas.applicazionebancaria.service.ClienteService;
import com.tas.applicazionebancaria.utils.JWT;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AdminController {
	@Autowired
	AmministratoreService as;
	@Autowired
	ClienteService cs;
	
	private static String getToken(HttpServletRequest request) {
		Cookie cookies[] = request.getCookies();
		for (Cookie c : cookies) {
			if (c.getName().equals("token")) {
				return c.getValue();
			}
		} 
		return null;
	}
	
	private static boolean validateInputs(Amministratore admin) {
		if (!admin.getNomeAdmin().matches("^[a-zA-Z ,.'-]{2,30}$")) return false;
		if (!admin.getCognomeAdmin().matches("^[a-zA-Z ,.'-]{2,30}$")) return false;
		if (!admin.getEmailAdmin().matches("^[\\w.%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) return false;
		if (!admin.getPasswordAdmin().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#&%^$?=])[a-zA-Z0-9@#&%^$?=]{7,15}$")) return false;
		return true;
	}
	
	@GetMapping("homeAdmin")
	public ModelAndView homeAdmin(HttpServletRequest request) {
		String token = getToken(request);
		
		try {
			JWT.validate(token);
		} catch (Exception exc) {
			return new ModelAndView("redirect:/loginAdmin");
		}
		
		List<Cliente> clienti = cs.findAll();
		ModelAndView mv = new ModelAndView("homeAdmin");
		mv.addObject("listaClienti", clienti);
		return mv;
	}
}
