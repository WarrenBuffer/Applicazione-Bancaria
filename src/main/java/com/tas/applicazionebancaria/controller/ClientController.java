package com.tas.applicazionebancaria.controller;

import java.util.Date;
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
import com.tas.applicazionebancaria.businesscomponent.model.Conto;
import com.tas.applicazionebancaria.businesscomponent.model.MovimentiConto;
import com.tas.applicazionebancaria.businesscomponent.model.Transazioni;
import com.tas.applicazionebancaria.businesscomponent.model.enumerations.TipoMovimento;
import com.tas.applicazionebancaria.businesscomponent.model.enumerations.TipoTransazione;
import com.tas.applicazionebancaria.config.BCryptEncoder;
import com.tas.applicazionebancaria.config.LoginAttemptService;
import com.tas.applicazionebancaria.service.ClienteMongoService;
import com.tas.applicazionebancaria.service.ClienteService;
import com.tas.applicazionebancaria.service.ContoService;
import com.tas.applicazionebancaria.service.MovimentiContoService;
import com.tas.applicazionebancaria.service.TransazioniService;
import com.tas.applicazionebancaria.utils.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
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
	ContoService contoService;
	
	@Autowired
	MovimentiContoService movimentiContoService;
	
	@Autowired
	TransazioniService transazioniService;
	
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
		System.out.println("sono dentro la validazione");
		if (!cliente.getNomeCliente().matches("^[a-zA-Z ,.'-]{2,30}$") || cliente.getNomeCliente().trim().isEmpty()
				|| cliente.getNomeCliente() == null) {
			return "Il campo nome non può essere vuoto e deve contenere solo lettere";
		}

		if (!cliente.getCognomeCliente().matches("^[a-zA-Z ,.'-]{2,30}$")
				|| cliente.getCognomeCliente().trim().isEmpty() || cliente.getCognomeCliente() == null) {
			return "Il campo cognome non può essere vuoto e deve contenere solo lettere";
		}

		if (!cliente.getEmailCliente().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
				|| cliente.getEmailCliente().trim().isEmpty() || cliente.getEmailCliente() == null) {
			return "Il campo email non può essere vuoto e deve essere un indirizzo email valido";
		}

		if (!cliente.getPasswordCliente()
				.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#&%^$?!=])[a-zA-Z0-9@#&%^$?!=]{7,15}$")
				|| cliente.getPasswordCliente().trim().isEmpty() || cliente.getPasswordCliente() == null) {
			return "Il campo password non può essere vuoto e deve rispettare i criteri di complessità";
		}

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
	
	/*-----------------------------------------HOME PAGE-----------------------------------------*/
	
	@GetMapping(value="/")
	public ModelAndView homePage(@CookieValue(name="token",required=false) String token) {
		if (token!=null) {
			return new ModelAndView("redirect:/home");
		}
		ModelAndView mv = new ModelAndView();
		mv.setViewName("homepage");
		return mv;
	}
	

	/*-----------------------------------------REGISTRAZIONE-----------------------------------------*/

	@GetMapping(value = "/registrazione")
	public ModelAndView registrazione(@CookieValue(name = "token", required = false) String token,
			HttpServletRequest request) {
		if (token != null) {
			return new ModelAndView("redirect:/home");
		}
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("registrazione");
		mv.addObject("cliente", new Cliente());
		return mv;
	}

	@PostMapping(value = "/registrazione")
	public ModelAndView registrazione(Cliente cliente, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		List<Cliente> list = clienteService.findAll();
		System.out.println(" Post registrazione");
		for (Cliente a : list) {
			if (a.getEmailCliente().equals(cliente.getEmailCliente())) {
				System.out.println("Email confronto" + a.getEmailCliente());
				mv.addObject("checkUser", "Utente già registrato");
				mv.setViewName("registrazione");
				return mv;
			}
		}
		String nome = checkEscapeHTML(cliente.getNomeCliente());
		String cognome = checkEscapeHTML(cliente.getCognomeCliente());
		String password = checkEscapeHTML(cliente.getPasswordCliente());
		String email = checkEscapeHTML(cliente.getEmailCliente());
		if (validateInputs(cliente).equals("ok")) {
			//System.out.println("validi");
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
			// System.out.println("non vanno bene");
			mv.addObject("validInputs", "Campi non validi");
			mv.setViewName("registrazione");
			return mv;
		}
	}

	/*-----------------------------------------LOGIN-----------------------------------------*/

	@GetMapping(value = "/login")
	public ModelAndView login(@CookieValue(name = "token", required = false) String token, HttpServletRequest request) {
		if (token != null) {
			return new ModelAndView("redirect:/home");
		}
		ModelAndView mv = new ModelAndView();
		mv.setViewName("login");
		return mv;
	}

	@PostMapping(value = "/login")
	public ModelAndView controlloLogin(@RequestParam("email") String email,
			@RequestParam("password") String password, HttpServletResponse response,
			HttpServletRequest request) {
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
				ModelAndView mv = new ModelAndView();
				// aggiorna i campi per i tentativi errati
				if (loginAttemptService.isBlocked(email)) {
					System.out.println("is blocked?");
					mv.addObject("error", "Numero di tentativi finiti! Contatta l'amministratore");
				} else {
					loginAttemptService.loginFailed(email);
					mv.addObject("error", "Password errata!");
				}
				mv.setViewName("login");
				return mv;
			}
		} else {
			ModelAndView mv = new ModelAndView();
			mv.addObject("error", "Utente non trovato!");
			mv.setViewName("login");
			return mv;
		}
	}
	
	/*-----------------------------------------HOME UTENTE-----------------------------------------*/
	

	@GetMapping("/home")
	public ModelAndView home(@CookieValue(name = "token", required = false) String token, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		try {
			JWT.validate(token);
		} catch (Exception exc) {
			return new ModelAndView("redirect:/registrazione");
		}

		Jws<Claims> claims = JWT.validate(token);
		mv.addObject("nome" ,claims.getBody().get("nome"));
			
		Optional<Cliente> c = clienteService.findByEmail(claims.getBody().getSubject().toString());
		List<Conto> listaConti = contoService.findByIdCliente(c.get().getCodCliente());
		if(listaConti!=null && !listaConti.isEmpty()) {
			mv.addObject("listaConti", listaConti);
			mv.setViewName("home");
			return mv;
		}
		

		mv.setViewName("home");
		return mv;
	}
	
	
	/*-----------------------------------------CONTO-----------------------------------------*/
	
	
	@GetMapping(value="/visualizzaconti")
	public ModelAndView visualizzaConto(@CookieValue(name="token",required=false) String token, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		try {	
			JWT.validate(token);
		}catch(Exception exc) {
			return new ModelAndView("redirect:/registrazione");
//			throw new TokenException(exc);
		}
		Jws<Claims> claims = JWT.validate(token);
		Optional<Cliente> c = clienteService.findByEmail(claims.getBody().getSubject().toString());
			List<Conto> listaConti = contoService.findByIdCliente(c.get().getCodCliente());
			if(listaConti!=null && !listaConti.isEmpty()) {
				mv.addObject("listaConti", listaConti);
				mv.setViewName("visualizzaconti");
				return mv;
			}else {
				mv.setViewName("visualizzaconti");
				return mv;
			}
		}
	
	@GetMapping(value="/creaconto")
	public ModelAndView creaConto(@CookieValue(name="token",required=false) String token, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		try {	
			JWT.validate(token);
		}catch(Exception exc) {
			return new ModelAndView("redirect:/registrazione");
//			throw new TokenException(exc);
		}
		
		mv.addObject("conto", new Conto());
		mv.setViewName("creaconto");
		return mv;
	}
	
	@PostMapping(value="/confermaconto")
	public ModelAndView confermaConto(Conto conto, @CookieValue(name="token",required=false) String token, HttpServletRequest request) {
		Jws<Claims> claims = JWT.validate(token);
		Optional<Cliente> c = clienteService.findByEmail(claims.getBody().getSubject().toString());
		if(c.isPresent()) {
			conto.setCodCliente(c.get());
			conto.setEmailCliente(c.get().getEmailCliente());
			conto.setSaldo(0.00);
			contoService.saveConto(conto);
		}
		return new ModelAndView("redirect:/visualizzaconti");
	}
	
	
	/*-----------------------------------------TRANSAZIONI-----------------------------------------*/
	
	@PostMapping(value="/deposito")
	public ModelAndView deposita(@RequestParam("codConto") long codConto,@RequestParam("importo") double importo, @CookieValue(name="token",required=false) String token, HttpServletRequest request) {
		try {	
			JWT.validate(token);
		}catch(Exception exc) {
			return new ModelAndView("redirect:/registrazione");
//			throw new TokenException(exc);
		}
		Optional<Conto> conto = contoService.findById(codConto);
		if(conto.isPresent()) {
			double saldo = conto.get().getSaldo();
			saldo+=importo;
			conto.get().setSaldo(saldo);
			contoService.saveConto(conto.get());
			
			MovimentiConto mc = new MovimentiConto();
			mc.setCodConto(conto.get().getCodConto());
			mc.setDataMovimento(new Date());
			mc.setImporto(importo);
			mc.setTipoMovimento(TipoMovimento.ACCREDITO);
			
			Transazioni t = new Transazioni();
			t.setCodConto(conto.get());
			t.setDataTransazione(new Date());
			t.setImporto(importo);
			t.setTipoTransazione(TipoTransazione.ACCREDITO);
			
			movimentiContoService.saveMovimentiConto(mc);
			transazioniService.saveTransazioni(t);
		}
		return new ModelAndView("redirect:/home");
	}

}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

