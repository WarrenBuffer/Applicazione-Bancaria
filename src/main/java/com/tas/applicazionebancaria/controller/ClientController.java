package com.tas.applicazionebancaria.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.servlet.ModelAndView;

//import com.jayway.jsonpath.Option;
import com.tas.applicazionebancaria.businesscomponent.model.CarteDiCredito;
import com.tas.applicazionebancaria.businesscomponent.model.Cliente;
import com.tas.applicazionebancaria.businesscomponent.model.ClienteMongo;
import com.tas.applicazionebancaria.businesscomponent.model.Conto;
import com.tas.applicazionebancaria.businesscomponent.model.MovimentiConto;

import com.tas.applicazionebancaria.businesscomponent.model.Prestiti;
import com.tas.applicazionebancaria.businesscomponent.model.RichiestePrestito;

import com.tas.applicazionebancaria.businesscomponent.model.Pagamenti;

import com.tas.applicazionebancaria.businesscomponent.model.Transazioni;
import com.tas.applicazionebancaria.businesscomponent.model.TransazioniBancarie;
import com.tas.applicazionebancaria.businesscomponent.model.TransazioniMongo;

import com.tas.applicazionebancaria.businesscomponent.model.enumerations.StatoPrestito;

import com.tas.applicazionebancaria.businesscomponent.model.enumerations.MetodoPagamento;
import com.tas.applicazionebancaria.businesscomponent.model.enumerations.TipoConto;

import com.tas.applicazionebancaria.businesscomponent.model.enumerations.TipoMovimento;
import com.tas.applicazionebancaria.businesscomponent.model.enumerations.TipoTransazione;
import com.tas.applicazionebancaria.config.BCryptEncoder;

//import com.tas.applicazionebancaria.config.LoginAttemptService;
import com.tas.applicazionebancaria.service.CarteDiCreditoService;
import com.tas.applicazionebancaria.service.ClienteMongoService;
import com.tas.applicazionebancaria.service.ClienteService;

import com.tas.applicazionebancaria.service.ContoService;
import com.tas.applicazionebancaria.service.MovimentiContoService;

import com.tas.applicazionebancaria.service.PrestitiService;
import com.tas.applicazionebancaria.service.RichiestePrestitoService;

import com.tas.applicazionebancaria.service.PagamentiService;
import com.tas.applicazionebancaria.service.TransazioniBancarieService;

import com.tas.applicazionebancaria.service.TransazioniMongoService;
import com.tas.applicazionebancaria.service.TransazioniService;

import com.tas.applicazionebancaria.utils.AccountBlocker;
import com.tas.applicazionebancaria.utils.EmailService;
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
	AccountBlocker accountBlocker;
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
	EmailService emailService;

	@Autowired
	TransazioniMongoService transazioniMongoService;

	@Autowired
	CarteDiCreditoService carteDiCreditoService;

	@Autowired
	PrestitiService prestitiService;

	@Autowired
	RichiestePrestitoService richiestePrestitoService;

	@Autowired
	PagamentiService pagamentiService;

	@Autowired
	TransazioniBancarieService transazioniBancarieService;

	@Autowired
	// LoginAttemptService loginAttemptService;

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
		// System.out.println("sono dentro la validazione");
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

	@GetMapping(value = "/")
	public ModelAndView homePage(@CookieValue(name = "token", required = false) String token) {
		if (token != null) {
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
				mv.addObject("checkUser", "Utente già registrato! Effettua il login.");
				mv.setViewName("registrazione");
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

			clienteService.saveCliente(cliente);
			// emailService.sendEmail(cliente.getEmailCliente(), "BENVENUTO IN APPLICAZIONE
			// BANCARIA", "PROVA");

			cliente = clienteService.saveCliente(cliente);

			ClienteMongo clienteMongo = new ClienteMongo();
			clienteMongo.setNomeCliente(cliente.getNomeCliente());
			clienteMongo.setCognomeCliente(cliente.getCognomeCliente());
			clienteMongo.setPasswordCliente(cliente.getPasswordCliente());
			clienteMongo.setEmailCliente(cliente.getEmailCliente());
			clienteMongo.setCodCliente(cliente.getCodCliente());

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
	public ModelAndView controlloLogin(@RequestParam("email") String email, @RequestParam("password") String password,
			HttpServletResponse response, HttpServletRequest request) {
		Optional<Cliente> c = clienteService.findByEmail(email);
		// (System.out.println(c.get().toString());

		if (c.isEmpty()) {
			ModelAndView mv = new ModelAndView();
			mv.addObject("error", "Utente non trovato!");
			mv.setViewName("login");
			return mv;
		}
		Cliente cliente = c.get();
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		if (encoder.matches(password, cliente.getPasswordCliente()) && !cliente.isAccountBloccato()) {
			String token = JWT.generate(cliente.getNomeCliente(), cliente.getCognomeCliente(),
					cliente.getEmailCliente());
			Cookie cookie = new Cookie("token", token);
			response.addCookie(cookie);
			HttpSession session = request.getSession();
			session.setAttribute("email_log", cliente.getEmailCliente());
			
			//resetto i tentativi errati in caso il cliente effettui il login
			accountBlocker.validClient(email);
			return new ModelAndView("redirect:/home");
		} else {
			ModelAndView mv = new ModelAndView();
			// aggiorna i campi per i tentativi errati
			if (cliente.isAccountBloccato()) {
				System.out.println("is blocked?");
				mv.addObject("error", "Numero di tentativi finiti! Contatta l'amministratore.");
			} else {
				accountBlocker.invalidClient(cliente.getEmailCliente());
				mv.addObject("error", "Password errata!");
			}
			mv.setViewName("login");
			return mv;
		}
	}

/*-----------------------------------------LOGOUT-----------------------------------------*/
	
	@PostMapping(value = "/logout")
	public ModelAndView effettuaLogout(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("token")) {
					cookie.setMaxAge(0);
					response.addCookie(cookie);					
				}
			}
		}
		request.getSession().invalidate();
		return new ModelAndView("redirect:/login");
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
		mv.addObject("nome" ,claims.getBody().get("nome"));
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
	
	@PostMapping(value="/selconto")
	public ModelAndView selConto(@RequestParam("codConto") long codConto,@CookieValue(name="token",required=false) String token, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		Optional<Conto> conto = contoService.findById(codConto);
		if(conto.isPresent()) {
			Jws<Claims> claims = JWT.validate(token);
			mv.addObject("nome" ,claims.getBody().get("nome"));
			Optional<Cliente> c = clienteService.findByEmail(claims.getBody().getSubject().toString());
			mv.addObject("cliente", c.get());
			List<Conto> listaConti = contoService.findByIdCliente(c.get().getCodCliente());
			if(listaConti!=null && !listaConti.isEmpty()) {
				mv.addObject("listaConti", listaConti);
				mv.addObject("conto",conto.get());
				mv.setViewName("visualizzaconti");
			}
			List<MovimentiConto> listaMovimenti = movimentiContoService.findUltimi10(codConto);
			if(listaMovimenti!= null && !listaMovimenti.isEmpty()) {
				mv.addObject("listaMovimenti",listaMovimenti);
			}
			
			List<TransazioniBancarie> listaTransazioni= transazioniBancarieService.findUltime10(codConto);
			
			if(listaTransazioni != null && !listaTransazioni.isEmpty()) {
				mv.addObject("listaTransazioni",listaTransazioni);
			}
			
			return mv;
		}
		mv.setViewName("visualizzaconti");
		return mv;
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
		
		Jws<Claims> claims = JWT.validate(token);
		mv.addObject("nome" ,claims.getBody().get("nome"));
		mv.addObject("conto", new Conto());
		mv.setViewName("creaconto");
		return mv;
	}
	
	@PostMapping(value="/confermaconto")
	public ModelAndView confermaConto(@RequestParam("tipoConto") TipoConto tipoConto , @CookieValue(name="token",required=false) String token, HttpServletRequest request) {
		Jws<Claims> claims = JWT.validate(token);
		Optional<Cliente> c = clienteService.findByEmail(claims.getBody().getSubject().toString());
		if(c.isPresent()) {
			Conto conto = new Conto();
			conto.setCodCliente(c.get().getCodCliente());
			conto.setEmailCliente(c.get().getEmailCliente());
			conto.setTipoConto(tipoConto);
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
		ModelAndView mv = new ModelAndView();
		Jws<Claims> claims = JWT.validate(token);
		mv.addObject("nome" ,claims.getBody().get("nome"));
		Optional<Cliente> c = clienteService.findByEmail(claims.getBody().getSubject().toString());
		
		Optional<Conto> conto = contoService.findById(codConto);
		if(conto.isPresent()) {
			double saldo = conto.get().getSaldo();
			saldo+=importo;
			conto.get().setSaldo(saldo);
			Conto con = contoService.saveConto(conto.get());
			
			//TODO 
			Optional<ClienteMongo> clienteM = clienteMongoService.findByEmail(c.get().getEmailCliente());
			clienteM.get().setSaldo(con.getSaldo());
			clienteMongoService.saveClienteMongo(clienteM.get());
			
			MovimentiConto mc = new MovimentiConto();
			mc.setCodConto(conto.get().getCodConto());
			mc.setDataMovimento(new Date());
			mc.setImporto(importo);
			mc.setTipoMovimento(TipoMovimento.ACCREDITO);
			
			Transazioni t = new Transazioni();
			t.setCodConto(conto.get().getCodConto());
			t.setDataTransazione(new Date());
			t.setImporto(importo);
			t.setTipoTransazione(TipoTransazione.ACCREDITO);
			
			movimentiContoService.saveMovimentiConto(mc);
			t = transazioniService.saveTransazioni(t);
			
			TransazioniMongo tm = new TransazioniMongo();
			tm.setCodiceConto(t.getCodConto());
			tm.setCodTransazione(t.getCodTransazione());
			tm.setDataTransazione(t.getDataTransazione());
			tm.setImporto(t.getImporto());
			tm.setTipoTransazione(t.getTipoTransazione());
			
			transazioniMongoService.saveTransazioniMongo(tm);
	
			mv.addObject("cliente", c.get());
			List<Conto> listaConti = contoService.findByIdCliente(c.get().getCodCliente());
			if(listaConti!=null && !listaConti.isEmpty()) {
				mv.addObject("listaConti", listaConti);
				mv.addObject("conto",conto.get());
			}
			List<MovimentiConto> listaMovimenti = movimentiContoService.findUltimi10(codConto);
			if(listaMovimenti!= null && !listaMovimenti.isEmpty()) {
				mv.addObject("listaMovimenti",listaMovimenti);
			}
			
			List<TransazioniBancarie> listaTransazioni= transazioniBancarieService.findUltime10(codConto);
			
			if(listaTransazioni != null && !listaTransazioni.isEmpty()) {
				mv.addObject("listaTransazioni",listaTransazioni);
			}
			mv.setViewName("visualizzaconti");
			mv.addObject("success", "Deposito andato a buon fine!");
			return mv;
			
		}
		return new ModelAndView("redirect:/home");
	}
	
	@PostMapping(value="/preleva")
	public ModelAndView preleva(@RequestParam("codConto") long codConto,@RequestParam("importo") double importo, @CookieValue(name="token",required=false) String token, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		try {	
			JWT.validate(token);
		}catch(Exception exc) {
			return new ModelAndView("redirect:/registrazione");
//			throw new TokenException(exc);
		}
		Jws<Claims> claims = JWT.validate(token);
		mv.addObject("nome" ,claims.getBody().get("nome"));
		Optional<Cliente> c = clienteService.findByEmail(claims.getBody().getSubject().toString());
		mv.addObject("cliente", c.get());
		Optional<Conto> conto = contoService.findById(codConto);
		if(conto.isPresent()) {
			if(conto.get().getSaldo()<importo) {
				mv.addObject("error", "Impossibile prelevare questa cifra! Saldo insufficiente.");
				List<MovimentiConto> listaMovimenti = movimentiContoService.findUltimi10(codConto);
				if(listaMovimenti!= null && !listaMovimenti.isEmpty()) {
					mv.addObject("listaMovimenti",listaMovimenti);
				}
				List<Conto> listaConti = contoService.findByIdCliente(c.get().getCodCliente());
				if(listaConti!=null && !listaConti.isEmpty()) {
					mv.addObject("listaConti", listaConti);
					mv.addObject("conto",conto.get());
				}
				
				mv.setViewName("visualizzaconti");
				return mv;
			}
			else {
				double saldo = conto.get().getSaldo();
				saldo -= importo;
				conto.get().setSaldo(saldo);
				Conto con = contoService.saveConto(conto.get());
				
				
				Optional<ClienteMongo> clienteM = clienteMongoService.findByEmail(c.get().getEmailCliente());
				clienteM.get().setSaldo(con.getSaldo());
				clienteMongoService.saveClienteMongo(clienteM.get());
				
				
				MovimentiConto mc = new MovimentiConto();
				mc.setCodConto(conto.get().getCodConto());
				mc.setDataMovimento(new Date());
				mc.setImporto(importo);
				mc.setTipoMovimento(TipoMovimento.ADDEBITO);
				
				Transazioni t = new Transazioni();
				t.setCodConto(conto.get().getCodConto());
				t.setDataTransazione(new Date());
				t.setImporto(importo);
				t.setTipoTransazione(TipoTransazione.ADDEBITO);
				
				movimentiContoService.saveMovimentiConto(mc);
				t = transazioniService.saveTransazioni(t);
				
				TransazioniMongo tm = new TransazioniMongo();
				tm.setCodiceConto(t.getCodConto());
				tm.setCodTransazione(t.getCodTransazione());
				tm.setDataTransazione(t.getDataTransazione());
				tm.setImporto(t.getImporto());
				tm.setTipoTransazione(t.getTipoTransazione());
				
				transazioniMongoService.saveTransazioniMongo(tm);
				
				
				List<Conto> listaConti = contoService.findByIdCliente(c.get().getCodCliente());
				if(listaConti!=null && !listaConti.isEmpty()) {
					mv.addObject("listaConti", listaConti);
					mv.addObject("conto",conto.get());
					mv.setViewName("visualizzaconti");
				}
				List<MovimentiConto> listaMovimenti = movimentiContoService.findUltimi10(codConto);
				if(listaMovimenti!= null && !listaMovimenti.isEmpty()) {
					mv.addObject("listaMovimenti",listaMovimenti);
				}
				
				List<TransazioniBancarie> listaTransazioni= transazioniBancarieService.findUltime10(codConto);
				
				if(listaTransazioni != null && !listaTransazioni.isEmpty()) {
					mv.addObject("listaTransazioni",listaTransazioni);
				}
				
				mv.addObject("success","Prelievo andato a buon fine!");
				return mv;
			}
		}
		return new ModelAndView("redirect:/home");
	}
	
	/*-----------------------------------------CARTE DI CREDITO-----------------------------------------*/

	@GetMapping(value="visualizzacarte")
	public ModelAndView visualizzaCarte(@CookieValue(name="token",required=false) String token, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		try {	
			JWT.validate(token);
		}catch(Exception exc) {
			return new ModelAndView("redirect:/registrazione");
//			throw new TokenException(exc);
		}
		Jws<Claims> claims = JWT.validate(token);
		mv.addObject("nome" ,claims.getBody().get("nome"));
		Optional<Cliente> c = clienteService.findByEmail(claims.getBody().getSubject().toString());
		mv.addObject("cliente", c.get());
		List<CarteDiCredito> listaCarte = carteDiCreditoService.findByCodCliente(c.get().getCodCliente());
			if(listaCarte!=null && !listaCarte.isEmpty()) {
				mv.addObject("listaCarte", listaCarte);
				mv.setViewName("visualizzacarte");
				return mv;
			}else {
				mv.setViewName("visualizzacarte");
				return mv;
			}
	}
	
	@GetMapping(value="/richiedicarta")
	public ModelAndView richiediCarta(@CookieValue(name="token",required=false) String token, HttpServletRequest request) {
		try {	
			JWT.validate(token);
		}catch(Exception exc) {
			return new ModelAndView("redirect:/registrazione");
//			throw new TokenException(exc);
		}
		
		Jws<Claims> claims = JWT.validate(token);
		Optional<Cliente> c = clienteService.findByEmail(claims.getBody().getSubject().toString());
		
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(new Date());
	    calendar.add(Calendar.YEAR, 5);
	    Date scadenza = calendar.getTime();
	    
	    Random random = new Random();
        int cvv = 100 + random.nextInt(900);
        
        String numerocarta = "1111 0000";
        
        int first = 1000 + random.nextInt(9000);
        int second = 1000 + random.nextInt(9000);
	    numerocarta = numerocarta + " " +String.valueOf(first) + " " + String.valueOf(second);
		CarteDiCredito carta = new CarteDiCredito();
		carta.setCodCliente(c.get().getCodCliente());
		carta.setDataScadenza(scadenza);
		carta.setCvv(String.valueOf(cvv));
		carta.setNumeroCarta(numerocarta);
		carteDiCreditoService.saveCarteDiCredito(carta);
		
		return new ModelAndView("redirect:/visualizzacarte");
	}
	
	@PostMapping(value="/selcarte")
	public ModelAndView selCarta(@RequestParam("codCarta") long codCarta,@CookieValue(name="token",required=false) String token, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		Optional<CarteDiCredito> carta = carteDiCreditoService.findById(codCarta);
		
		if(carta.isPresent()) {
			Jws<Claims> claims = JWT.validate(token);
			mv.addObject("nome" ,claims.getBody().get("nome"));
			Optional<Cliente> c = clienteService.findByEmail(claims.getBody().getSubject().toString());
			mv.addObject("cliente", c.get());
			List<CarteDiCredito> listaCarte = carteDiCreditoService.findByCodCliente(c.get().getCodCliente());
			if(listaCarte!=null && !listaCarte.isEmpty()) {
				mv.addObject("listaCarte", listaCarte);
				mv.addObject("carta",carta.get());
				mv.setViewName("visualizzacarte");
			}
			return mv;
		}
		mv.setViewName("visualizzacarte");
		return mv;
	}

	@PostMapping(value="/eliminacarta")
	public ModelAndView eliminaCarta(@RequestParam("codCarta") long codCarta, @CookieValue(name="token",required=false) String token, HttpServletRequest request) {
		Optional<CarteDiCredito> carta = carteDiCreditoService.findById(codCarta);
		if(carta.isPresent()) {
			carteDiCreditoService.deleteCarteDiCredito(carta.get());
		}
		
		return new ModelAndView("redirect:/visualizzacarte");
	}
	
	/*-----------------------------------------PAGAMENTI-----------------------------------------*/
	
	@GetMapping(value="/iniziopagamento")
	public ModelAndView iniziaPagamento(@CookieValue(name="token",required=false) String token, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		try {	
			JWT.validate(token);
		}catch(Exception exc) {
			return new ModelAndView("redirect:/registrazione");
//			throw new TokenException(exc);
		}
		
		Jws<Claims> claims = JWT.validate(token);
		mv.addObject("nome" ,claims.getBody().get("nome"));
		Optional<Cliente> c = clienteService.findByEmail(claims.getBody().getSubject().toString());
		
		List<Conto> listaConti = contoService.findByIdCliente(c.get().getCodCliente());
		if(listaConti!=null && !listaConti.isEmpty()) {
			mv.addObject("listaConti", listaConti);
		}
		List<CarteDiCredito> listaCarte = carteDiCreditoService.findByCodCliente(c.get().getCodCliente());
		if(listaCarte!=null && !listaCarte.isEmpty()) {
			mv.addObject("listaCarte", listaCarte);
		}
		
		
		mv.setViewName("pagamento");

		return mv;
	}

	@PostMapping(value="/selcontoPagamento")
	public ModelAndView selContoPagamento(@RequestParam("codConto") long codConto,@CookieValue(name="token",required=false) String token, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		
		Optional<Conto> conto = contoService.findById(codConto);
		if(conto.isPresent()) {
			Jws<Claims> claims = JWT.validate(token);
			mv.addObject("nome" ,claims.getBody().get("nome"));
			Optional<Cliente> c = clienteService.findByEmail(claims.getBody().getSubject().toString());
			List<Conto> listaConti = contoService.findByIdCliente(c.get().getCodCliente());
			if(listaConti!=null && !listaConti.isEmpty()) {
				mv.addObject("listaConti", listaConti);
				mv.addObject("codConto",conto.get().getCodConto());
			}
			List<CarteDiCredito> listaCarte = carteDiCreditoService.findByCodCliente(c.get().getCodCliente());
			if(listaCarte!=null && !listaCarte.isEmpty()) {
				mv.addObject("listaCarte", listaCarte);
			}
			
			mv.setViewName("pagamento");
			return mv;
		}
		mv.setViewName("pagamento");
		return mv;
	}
	
	
	@PostMapping(value="/pagamento")
	public ModelAndView pagamento(@RequestParam("metodoPagamento") MetodoPagamento metodo,@RequestParam("importo") double importo, 
			String codConto, @RequestParam("contoDest") long contoDest, @CookieValue(name="token",required=false) String token, HttpServletRequest request) {
		try {	
			JWT.validate(token);
		}catch(Exception exc) {
			return new ModelAndView("redirect:/registrazione");
//			throw new TokenException(exc);
		}
		ModelAndView mv = new ModelAndView();
		Jws<Claims> claims = JWT.validate(token);
		mv.addObject("nome" ,claims.getBody().get("nome"));
		if(codConto=="") {
			mv.addObject("error","ERRORE! Selezionare un conto!");
			mv.setViewName("home");
			return mv;
		}
	
		Optional<Conto> contoS = contoService.findById(Long.valueOf(codConto));
		
		if(importo>contoS.get().getSaldo()) {
			mv.addObject("error", "Saldo insufficiente per trasferire questa cifra!");
			mv.setViewName("home");
			return mv;
		}
		
		
		
		Optional<Conto> contoD = contoService.findById(Long.valueOf(contoDest));
		if(contoD.isPresent() && contoS.isPresent()) {
			Optional<Cliente> c = clienteService.findByEmail(claims.getBody().getSubject().toString());
			
			//Aggiorno saldi
			double saldo= contoS.get().getSaldo();
			saldo=saldo-importo;
			contoS.get().setSaldo(saldo);
			saldo=0;
			saldo = contoD.get().getSaldo();
			saldo = saldo + importo;
			contoD.get().setSaldo(saldo);
			
			Conto con = contoService.saveConto(contoS.get());
			Conto conD = contoService.saveConto(contoD.get());
			
			
			Optional<ClienteMongo> clienteM = clienteMongoService.findByEmail(c.get().getEmailCliente());
			Optional<Cliente> cliente = clienteService.findById(conD.getCodCliente());
			Optional<ClienteMongo> clienteM2 = clienteMongoService.findByEmail(cliente.get().getEmailCliente());
			clienteM.get().setSaldo(con.getSaldo());
			clienteM2.get().setSaldo(conD.getSaldo());
			clienteMongoService.saveClienteMongo(clienteM.get());
			clienteMongoService.saveClienteMongo(clienteM2.get());
			
			
			Pagamenti p = new Pagamenti();
			p.setCodCliente(c.get().getCodCliente());
			p.setDataPagamento(new Date());
			p.setImporto(importo);
			p.setMetodoPagamento(metodo);
			
			pagamentiService.savePagamenti(p);
			
			TransazioniBancarie tb = new TransazioniBancarie();
			tb.setDataTransazione(new Date());
			tb.setImporto(importo);
			tb.setContoOrigine(Long.valueOf(codConto));
			tb.setContoDestinazione(Long.valueOf(contoDest));
			tb.setTipoTransazione(TipoTransazione.ADDEBITO);
			
			TransazioniBancarie tbDest = new TransazioniBancarie();
			tbDest.setContoOrigine(Long.valueOf(codConto));
			tbDest.setContoDestinazione(Long.valueOf(contoDest));
			tbDest.setImporto(importo);
			tbDest.setDataTransazione(new Date());
			tbDest.setTipoTransazione(TipoTransazione.ACCREDITO);
			
			transazioniBancarieService.saveTransazioniBancarie(tb);
			transazioniBancarieService.saveTransazioniBancarie(tbDest);
			
			mv.addObject("success","Pagamento confermato!");
			
			mv.setViewName("home");
			return mv;
			
		}
		//Se error o success sono presenti, apre il modal
		mv.addObject("error","ERRORE! Il conto inserito non esiste!");
		mv.setViewName("home");
		return mv;
	}
	
	/*-----------------------------------------PRESTITI-----------------------------------------*/

	@GetMapping(value = "/visualizzaprestiti")
	public ModelAndView visualizzaPrestiti(@CookieValue(name="token",required=false) String token, HttpServletRequest request) {
		try {	
			JWT.validate(token);
		}catch(Exception exc) {
			return new ModelAndView("redirect:/registrazione");
	//		throw new TokenException(exc);
		}
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("visualizzaprestiti");
		Jws<Claims> claims = JWT.validate(token);
		Optional<Cliente> c = clienteService.findByEmail(claims.getBody().getSubject().toString());
		mv.addObject("nome" ,claims.getBody().get("nome"));
		
		List<Prestiti> listaPrestiti = prestitiService.findByCodCliente(c.get().getCodCliente());
		List<RichiestePrestito> listaRichiestePrestito = richiestePrestitoService.findByCodCliente(c.get().getCodCliente());
		if (listaPrestiti != null && !listaPrestiti.isEmpty()) {
			mv.addObject("listaPrestiti", listaPrestiti);
		}
		if (listaRichiestePrestito != null && !listaRichiestePrestito.isEmpty()) {
			mv.addObject("listaRichiestePrestito", listaRichiestePrestito);
		}
		return mv;
	}

	@PostMapping(value="/selprestito")
	public ModelAndView selPrestito(@RequestParam("codPrestito") long codPrestito,@CookieValue(name="token",required=false) String token, HttpServletRequest request) {
		try {	
			JWT.validate(token);
		}catch(Exception exc) {
			return new ModelAndView("redirect:/registrazione");
	//		throw new TokenException(exc);
		}
		
		ModelAndView mv = new ModelAndView();
		Optional<Prestiti> prestito = prestitiService.findById(codPrestito);
		
		if(prestito.isPresent()) {
			Jws<Claims> claims = JWT.validate(token);
			mv.addObject("nome" ,claims.getBody().get("nome"));
			Optional<Cliente> c = clienteService.findByEmail(claims.getBody().getSubject().toString());
			mv.addObject("cliente", c.get());
			List<Prestiti> listaPrestiti = prestitiService.findByCodCliente(c.get().getCodCliente());
			List<RichiestePrestito> listaRichiestePrestito = richiestePrestitoService.findByCodCliente(c.get().getCodCliente());
			if(listaPrestiti!=null && !listaPrestiti.isEmpty()) {
				mv.addObject("listaPrestiti", listaPrestiti);
				mv.addObject("prestito",prestito.get());
				mv.setViewName("visualizzaprestiti");
			}
			if (listaRichiestePrestito != null && !listaRichiestePrestito.isEmpty()) {
				mv.addObject("listaRichiestePrestito", listaRichiestePrestito);
				mv.setViewName("visualizzaprestiti");
			}
			return mv;
		}
		mv.setViewName("visualizzaprestiti");
		return mv;
	}

	@PostMapping(value = "/richiediprestito")
	public ModelAndView richiediPrestito(@RequestParam("importo") double importo,@CookieValue(name="token",required=false) String token, HttpServletRequest request) {
		try {	
			JWT.validate(token);
		}catch(Exception exc) {
			return new ModelAndView("redirect:/registrazione");
	//		throw new TokenException(exc);
		}
		
		Jws<Claims> claims = JWT.validate(token);
		Optional<Cliente> c = clienteService.findByEmail(claims.getBody().getSubject().toString());
		RichiestePrestito richiesta = new RichiestePrestito();
		richiesta.setCodCliente(c.get().getCodCliente());
		richiesta.setDataRichiesta(new Date());
		richiesta.setImporto(importo);
		richiesta.setStato(StatoPrestito.IN_ATTESA);
		richiestePrestitoService.saveRichiestePrestito(richiesta);
		return new ModelAndView("redirect:/visualizzaprestiti");
	}

}
