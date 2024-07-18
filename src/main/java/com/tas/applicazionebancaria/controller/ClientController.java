package com.tas.applicazionebancaria.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.servlet.ModelAndView;

import com.tas.applicazionebancaria.businesscomponent.model.Cliente;
import com.tas.applicazionebancaria.businesscomponent.model.ClienteMongo;
import com.tas.applicazionebancaria.config.BCryptEncoder;
import com.tas.applicazionebancaria.config.LoginAttemptService;
import com.tas.applicazionebancaria.service.ClienteMongoService;
import com.tas.applicazionebancaria.service.ClienteService;
import com.tas.applicazionebancaria.utils.JWT;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@SessionScope
public class ClientController {

	@Autowired
	ClienteService clienteService;

	@Autowired
	ClienteMongoService clienteMongoService;
	
	@Autowired
	LoginAttemptService loginAttemptService;

	/*-----------------------------------------UTILITIES-----------------------------------------*/

	public static Cookie getToken(HttpServletRequest request) {
		Cookie[] cookie = request.getCookies();
		for (Cookie c : cookie) {
			if (c.getName().equals("token")) {
				return c;
			}
		}
		return null;
	}

	private static String validateInputs(Cliente cliente) {
		if (!cliente.getNomeCliente().matches("^[a-zA-Z ,.'-]{2,30}$") || cliente.getNomeCliente().trim().isEmpty()
				|| cliente.getNomeCliente() == null)
			return "Il campo nome non può essere vuoto e deve contenere solo lettere";
		if (!cliente.getCognomeCliente().matches("^[a-zA-Z ,.'-]{2,30}$")
				|| cliente.getCognomeCliente().trim().isEmpty() || cliente.getCognomeCliente() == null)
			return "Il campo cognome non può essere vuoto e deve contenere solo lettere";
		if (!cliente.getEmailCliente().matches("^[\\w.%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
				|| cliente.getEmailCliente().trim().isEmpty() || cliente.getEmailCliente() == null)
			return "Il campo email non può essere vuoto e deve essere un indirizzo email valido";
		if (!cliente.getPasswordCliente()
				.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#&%^$?=])[a-zA-Z0-9@#&%^$?=]{7,15}$")
				|| cliente.getPasswordCliente().trim().isEmpty() || cliente.getPasswordCliente() == null)
			return "Il campo password non può essere vuoto e deve rispettare i criteri di complessità";

		return "ok";
	}

	private static String checkEscapeHTML(String input) {
		StringBuilder escaped = new StringBuilder();
		for (char c : input.toCharArray()) {
			switch (c) {
			case '<':
				escaped.append("&lt;");
				break;
			case '>':
				escaped.append("&gt;");
				break;
			case '&':
				escaped.append("&amp;");
				break;
			case '"':
				escaped.append("&quot;");
				break;
			case '\'':
				escaped.append("&#x27;");
				break;
			case '/':
				escaped.append("&#x2F;");
				break;
			default:
				escaped.append(c);
				break;
			}
		}
		return escaped.toString();
	}

	/*-----------------------------------------REGISTRAZIONE-----------------------------------------*/

	@GetMapping(value = "/registrazione")
	public ModelAndView registrazione(@CookieValue("token") String token, HttpServletRequest request) {
		if (JWT.validate(token) != null) {
			return new ModelAndView("redirect:/home");
		}
		ModelAndView mv = new ModelAndView();
		mv.setViewName("registrazione");
		mv.addObject("cliente", new Cliente());
		return mv;
	}

	@PostMapping(value = "/registrazione")
	public ModelAndView registrazione(Cliente cliente) {
		ModelAndView mv = new ModelAndView();
		List<Cliente> list = clienteService.findAll();
		for (Cliente a : list) {
			if (a.getEmailCliente().equals(cliente.getEmailCliente())) {
				mv.addObject("checkUser", "Utente già registrato");
				return mv;
			}
		}
		String nome = checkEscapeHTML(cliente.getNomeCliente());
		String cognome = checkEscapeHTML(cliente.getCognomeCliente());
		String password = checkEscapeHTML(cliente.getPasswordCliente());
		String email = checkEscapeHTML(cliente.getEmailCliente());

		if (validateInputs(cliente).equals("ok")) {
			cliente.setPasswordCliente(BCryptEncoder.encode(password));
			cliente.setNomeCliente(nome);
			cliente.setCognomeCliente(cognome);
			cliente.setEmailCliente(email);

			ClienteMongo clienteMongo = new ClienteMongo();
			clienteMongo.setNomeCliente(nome);
			clienteMongo.setCognomeCliente(cognome);
			clienteMongo.setPasswordCliente(BCryptEncoder.encode(password));
			clienteMongo.setEmailCliente(email);

			clienteService.saveCliente(cliente);
			clienteMongoService.saveClienteMongo(clienteMongo);
			return new ModelAndView("redirect:/login");
		} else {
			return new ModelAndView("redirect:/registrazione");
		}

	}

	/*-----------------------------------------LOGIN-----------------------------------------*/

	@GetMapping(value = "/login")
	public ModelAndView login(@CookieValue("token") String token, HttpServletRequest request) {
		if (JWT.validate(token) != null) {
			return new ModelAndView("redirect:/home");
		}
		ModelAndView mv = new ModelAndView();
		mv.setViewName("login");
		return mv;
	}

	@PostMapping(value = "/login")
	public ModelAndView controlloLogin(@RequestParam("email") String email, @RequestParam("password") String password,
			HttpServletResponse response, HttpServletRequest request) {
		Optional<Cliente> c = clienteService.findByEmail(email);
		if (c.isPresent()) {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			if (encoder.matches(password, c.get().getPasswordCliente())) {
				String token = JWT.generate(c.get().getNomeCliente(), c.get().getCognomeCliente(),
						c.get().getEmailCliente());
				Cookie cookie = new Cookie("token", token);
				response.addCookie(cookie);
				HttpSession session = request.getSession();
				session.setAttribute("email_log", c.get().getEmailCliente());
				return new ModelAndView("redirect:/home");
			} else {
				//aggiorna i campi per i tentativi errati
				loginAttemptService.loginFailed(email);
				ModelAndView mv = new ModelAndView();
				mv.addObject("error", "Credenziali errate!");
				mv.setViewName("login");
				return mv;
			}
		} else {
			ModelAndView mv = new ModelAndView();
			mv.addObject("error", "Credenziali errate!");
			mv.setViewName("login");
			return mv;
		}
	}
}
