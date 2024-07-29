package com.tas.applicazionebancaria.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.servlet.ModelAndView;

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

	public static String validateInputs(Cliente cliente) {
		// System.out.println("sono dentro la validazione");
		if (cliente.getNomeCliente() == null || cliente.getNomeCliente().trim().isEmpty()) {
			return "Il campo nome non può essere vuoto";
		}
		if(!cliente.getNomeCliente().matches("^[a-zA-Z ,.'-]{2,30}$")) {
			return "Il campo nome deve contenere solo lettere (da 2 a 30 caratteri)";
		}

		if (cliente.getCognomeCliente() == null || cliente.getCognomeCliente().trim().isEmpty()) {
			return "Il campo cognome non può essere vuoto";
		}
		if(!cliente.getCognomeCliente().matches("^[a-zA-Z ,.'-]{2,30}$")) {
			return "Il campo cognome deve contenere solo lettere (da 2 a 30 caratteri)";
		}

		if (cliente.getEmailCliente() == null || cliente.getEmailCliente().trim().isEmpty()) {
			return "Il campo email non può essere vuoto";
		}
		if(!cliente.getEmailCliente().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")){
			return "Il campo email deve essere un indirizzo email valido";
		}
		if (cliente.getPasswordCliente() == null || cliente.getPasswordCliente().trim().isEmpty()) {
			return "Il campo password non può essere vuoto";
		}
		if(!cliente.getPasswordCliente()
				.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#&%^$?!=])[a-zA-Z0-9@#&%^$?!=]{7,15}$")) {
			return "Il campo password deve rispettare i criteri di complessità";
		}

		return "ok";
	}

	public static String checkEscapeHTML(String input) {
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
		for (Cliente a : list) {
			if (a.getEmailCliente().equals(cliente.getEmailCliente())) {
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
			emailService.sendEmail(cliente.getEmailCliente(), "BENVENUTO IN APPLICAZIONE BANCARIA", "Benvenuto in applicazione bancaria, non siamo scammer!");

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
	public ModelAndView controlloLogin(@RequestParam("email") String email, @RequestParam("password") String password, HttpServletResponse response, HttpServletRequest request) {
		Optional<Cliente> c = clienteService.findByEmail(email);
		ModelAndView mv = new ModelAndView();

		if (c.isEmpty()) {
			mv.addObject("error", "Utente non trovato!");
			mv.setViewName("login");
			return mv;
		}
		Cliente cliente = c.get();
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		if (encoder.matches(password, cliente.getPasswordCliente())) {
			if (!cliente.isAccountBloccato()) {
				String token = JWT.generate(cliente.getNomeCliente(), cliente.getCognomeCliente(),cliente.getEmailCliente());
				Cookie cookie = new Cookie("token", token);
				response.addCookie(cookie);
				HttpSession session = request.getSession();
				session.setAttribute("email_log", cliente.getEmailCliente());
				accountBlocker.validClient(email);
				return new ModelAndView("redirect:/home");
			} else {
				mv.addObject("error", "Account bloccato! Contatta l'amministratore.");
				mv.setViewName("login");
				return mv;
			}
		} else {
			if (cliente.isAccountBloccato()) {
				mv.addObject("error", "Account bloccato! Contatta l'amministratore.");
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
		Cookie cookie = new Cookie("token", "");
		cookie.setMaxAge(0);
		response.addCookie(cookie);
		request.getSession().invalidate();
		return new ModelAndView("redirect:/login");
	}
	
	/*-----------------------------------------HOME UTENTE-----------------------------------------*/
	

	@GetMapping("/home")
	public ModelAndView home(@CookieValue(name = "token", required = false) String token, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		Jws<Claims> claims = JWT.validate(token);
		mv.addObject("nome" ,claims.getBody().get("nome"));
		mv.setViewName("home");
		return mv;
	}
	
	
	/*-----------------------------------------CONTO-----------------------------------------*/
	
	
	@GetMapping(value="/visualizzaconti")
	public ModelAndView visualizzaConto(@CookieValue(name="token",required=false) String token, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		
		Jws<Claims> claims = JWT.validate(token);
		mv.addObject("nome" ,claims.getBody().get("nome"));
		Optional<Cliente> c = clienteService.findByEmail(claims.getBody().getSubject().toString());
		if(!c.get().getConti().isEmpty()) {
			mv.addObject("listaConti", c.get().getConti());
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
			if(!c.get().getConti().isEmpty()) {
				mv.addObject("listaConti", c.get().getConti());
				mv.addObject("conto",conto.get());
			}
			List<MovimentiConto> listaMovimenti = movimentiContoService.findUltimi10(codConto);
			if(!listaMovimenti.isEmpty()) {
				mv.addObject("listaMovimenti",listaMovimenti);
			}
			
			if(conto.get().getTipoConto()==TipoConto.CORRENTE) {
				List<TransazioniBancarie> listaTransazioni= transazioniBancarieService.findUltime10(codConto);
				List<TransazioniBancarie> listaAccrediti = transazioniBancarieService.findUltimi10Accrediti(codConto);
				
				if(!listaTransazioni.isEmpty()) {
					listaTransazioni.addAll(listaAccrediti);
					mv.addObject("listaTransazioni",listaTransazioni);
				}
			}
		}
		mv.setViewName("visualizzaconti");
		return mv;
	}
	
	@PostMapping(value="/confermaconto")
	public ModelAndView confermaConto(@RequestParam("tipoConto") TipoConto tipoConto , @CookieValue(name="token",required=false) String token, HttpServletRequest request) {
		Jws<Claims> claims = JWT.validate(token);
		Optional<Cliente> c = clienteService.findByEmail(claims.getBody().getSubject().toString());
		if(c.isPresent()) {
			Conto conto = new Conto();
			conto.setCodCliente(c.get().getCodCliente());
			conto.setTipoConto(tipoConto);
			conto.setSaldo(0.00);
			contoService.saveConto(conto);
		}
		return new ModelAndView("redirect:/visualizzaconti");
	}
	
	
	/*-----------------------------------------TRANSAZIONI-----------------------------------------*/
	
	@PostMapping(value="/deposito")
	public ModelAndView deposita(@RequestParam("codConto") long codConto,@RequestParam("importo") double importo, @CookieValue(name="token",required=false) String token, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		Jws<Claims> claims = JWT.validate(token);
		mv.addObject("nome" ,claims.getBody().get("nome"));
		Optional<Cliente> c = clienteService.findByEmail(claims.getBody().getSubject().toString());
		mv.addObject("cliente", c.get());
		Optional<Conto> conto = contoService.findById(codConto);
		if(conto.isPresent()) {
			double saldo = conto.get().getSaldo();
			saldo+=importo;
			conto.get().setSaldo(saldo);
			Conto con = contoService.saveConto(conto.get());
			
			Optional<ClienteMongo> clienteM = clienteMongoService.findByEmail(c.get().getEmailCliente());
			clienteM.get().setSaldo(con.getSaldo());
			clienteM.get().setCodCliente(c.get().getCodCliente());
			clienteM.get().setEmailCliente(c.get().getEmailCliente());
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
			
			if(!c.get().getConti().isEmpty()) {
				mv.addObject("listaConti", c.get().getConti());
				mv.addObject("conto",conto.get());
			}
			List<MovimentiConto> listaMovimenti = movimentiContoService.findUltimi10(codConto);
			if(!listaMovimenti.isEmpty()) {
				mv.addObject("listaMovimenti",listaMovimenti);
			}
			
			List<TransazioniBancarie> listaTransazioni= transazioniBancarieService.findUltime10(codConto);
			if(!listaTransazioni.isEmpty()) {
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
		Jws<Claims> claims = JWT.validate(token);
		mv.addObject("nome" ,claims.getBody().get("nome"));
		Optional<Cliente> c = clienteService.findByEmail(claims.getBody().getSubject().toString());
		mv.addObject("cliente", c.get());
		Optional<Conto> conto = contoService.findById(codConto);
		if(conto.isPresent()) {
			if(conto.get().getSaldo()<importo) {
				mv.addObject("error", "Impossibile prelevare questa cifra! Saldo insufficiente.");
				List<MovimentiConto> listaMovimenti = movimentiContoService.findUltimi10(codConto);
				if(!listaMovimenti.isEmpty()) {
					mv.addObject("listaMovimenti",listaMovimenti);
				}
				if(!c.get().getConti().isEmpty()) {
					mv.addObject("listaConti", c.get().getConti());
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
				clienteMongoService.findAll().forEach(System.out::println);
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
				
				if(!c.get().getConti().isEmpty()) {
					mv.addObject("listaConti", c.get().getConti());
					mv.addObject("conto",conto.get());
				}
				List<MovimentiConto> listaMovimenti = movimentiContoService.findUltimi10(codConto);
				if(!listaMovimenti.isEmpty()) {
					mv.addObject("listaMovimenti",listaMovimenti);
				}
				
				List<TransazioniBancarie> listaTransazioni= transazioniBancarieService.findUltime10(codConto);
				if(!listaTransazioni.isEmpty()) {
					mv.addObject("listaTransazioni",listaTransazioni);
				}

				mv.setViewName("visualizzaconti");
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
		Jws<Claims> claims = JWT.validate(token);
		mv.addObject("nome" ,claims.getBody().get("nome"));
		Optional<Cliente> c = clienteService.findByEmail(claims.getBody().getSubject().toString());
		mv.addObject("cliente", c.get());
		if(!c.get().getCarte().isEmpty()) {
			mv.addObject("listaCarte", c.get().getCarte());		
		}
		
		mv.setViewName("visualizzacarte");
		return mv;
	}
	
	@GetMapping(value="/richiedicarta")
	public ModelAndView richiediCarta(@CookieValue(name="token",required=false) String token, HttpServletRequest request) {
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
		Jws<Claims> claims = JWT.validate(token);
		mv.addObject("nome" ,claims.getBody().get("nome"));
		if(carta.isPresent()) {
			Optional<Cliente> c = clienteService.findByEmail(claims.getBody().getSubject().toString());
			mv.addObject("cliente", c.get());
			if(!c.get().getCarte().isEmpty()) {
				mv.addObject("listaCarte", c.get().getCarte());
				mv.addObject("carta",carta.get());
			}
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
		Jws<Claims> claims = JWT.validate(token);
		mv.addObject("nome" ,claims.getBody().get("nome"));
		Optional<Cliente> c = clienteService.findByEmail(claims.getBody().getSubject().toString());
		
		Set<Conto> conti = c.get().getConti();
		if (!conti.isEmpty()) {
		    Iterator<Conto> iterator = conti.iterator();
		    while (iterator.hasNext()) {
		        Conto con = iterator.next();
		        if (con.getTipoConto() == TipoConto.RISPARMIO) {
		            iterator.remove();
		        }
		    }
		    mv.addObject("listaConti", conti);
		}
		if(!c.get().getCarte().isEmpty()) {
			mv.addObject("listaCarte", c.get().getCarte());
		}
		
		mv.setViewName("pagamento");
		return mv;
	}

	@PostMapping(value="/selcontoPagamento")
	public ModelAndView selContoPagamento(@RequestParam("codConto") long codConto,@CookieValue(name="token",required=false) String token, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		Optional<Conto> conto = contoService.findById(codConto);
		Jws<Claims> claims = JWT.validate(token);
		mv.addObject("nome" ,claims.getBody().get("nome"));
		if(conto.isPresent()) {
			Optional<Cliente> c = clienteService.findByEmail(claims.getBody().getSubject().toString());
			Set<Conto> conti = c.get().getConti();
			if (!conti.isEmpty()) {
				Iterator<Conto> iterator = conti.iterator();
				while (iterator.hasNext()) {
					Conto con = iterator.next();
					if (con.getTipoConto() == TipoConto.RISPARMIO) {
						iterator.remove();
			        }
			    }
			    mv.addObject("listaConti", conti);
				mv.addObject("codConto",conto.get().getCodConto());
				mv.addObject("conto", conto.get());
			}
			if(!c.get().getCarte().isEmpty()) {
				mv.addObject("listaCarte", c.get().getCarte());
			}
		}
		mv.setViewName("pagamento");
		return mv;
	}
	
	
	@PostMapping(value="/pagamento")
	public ModelAndView pagamento(@RequestParam("metodoPagamento") MetodoPagamento metodo,@RequestParam("importo") double importo, 
			String codConto, @RequestParam("contoDest") long contoDest, @CookieValue(name="token",required=false) String token, HttpServletRequest request) {
		
		ModelAndView mv = new ModelAndView();
		Jws<Claims> claims = JWT.validate(token);
		Optional<Cliente> c = clienteService.findByEmail(claims.getBody().getSubject().toString());
		mv.addObject("nome" ,claims.getBody().get("nome"));
		if(codConto.equals("")) {
			mv.addObject("error","ERRORE! Selezionare un conto!");
			mv.setViewName("home");
			return mv;
		}
		
		List<Conto> contiSorg = contoService.findByIdCliente(c.get().getCodCliente());
		for (Conto conto : contiSorg) {
			if (conto.getCodConto() == contoDest) {
				mv.addObject("error", "Non puoi selezionare uno dei tuoi conti!");
				mv.setViewName("home");
				return mv; 
			}
		}
		
		Optional<Conto> contoS = contoService.findById(Long.valueOf(codConto));
		Optional<Conto> contoD = contoService.findById(contoDest);
		if(contoD.isPresent() && contoS.isPresent()) {
			if(importo>contoS.get().getSaldo()) {
				mv.addObject("error", "Saldo insufficiente per trasferire questa cifra!");
				mv.setViewName("home");
				return mv;
			}
			double saldo= contoS.get().getSaldo();
			saldo=saldo-importo;
			contoS.get().setSaldo(saldo);
			saldo=0;
			saldo = contoD.get().getSaldo();
			saldo = saldo + importo;
			contoD.get().setSaldo(saldo);
			
			Conto con = contoService.saveConto(contoS.get());
			Conto conD = contoService.saveConto(contoD.get());
			
			Optional<Cliente> cliente = clienteService.findById(conD.getCodCliente());
			Optional<ClienteMongo> clienteM = clienteMongoService.findByEmail(c.get().getEmailCliente());
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
		mv.addObject("error","ERRORE! Il conto inserito non esiste!");
		mv.setViewName("home");
		return mv;
	}


	/*-----------------------------------------PRESTITI-----------------------------------------*/

	@GetMapping(value = "/visualizzaprestiti")
	public ModelAndView visualizzaPrestiti(@CookieValue(name = "token", required = false) String token,
			HttpServletRequest request) {
		Jws<Claims> claims = JWT.validate(token);

		ModelAndView mv = new ModelAndView();
		mv.setViewName("visualizzaprestiti");
		Optional<Cliente> c = clienteService.findByEmail(claims.getBody().getSubject().toString());
		mv.addObject("nome", claims.getBody().get("nome"));

		Set<Prestiti> listaPrestiti = c.get().getPrestiti();
		System.out.println(listaPrestiti);
		List<RichiestePrestito> listaRichiestePrestito = richiestePrestitoService
				.findByCodCliente(c.get().getCodCliente());
		if (!listaPrestiti.isEmpty()) {
			mv.addObject("listaPrestiti", listaPrestiti);
		}
		if (!listaRichiestePrestito.isEmpty()) {
			mv.addObject("listaRichiestePrestito", listaRichiestePrestito);
		}
		return mv;
	}

	@PostMapping(value = "/selprestito")
	public ModelAndView selPrestito(@RequestParam("codPrestito") long codPrestito,
			@CookieValue(name = "token", required = false) String token, HttpServletRequest request) {
		Jws<Claims> claims = JWT.validate(token);

		ModelAndView mv = new ModelAndView("visualizzaprestiti");
		Optional<Prestiti> prestito = prestitiService.findById(codPrestito);

		if (prestito.isPresent()) {
			mv.addObject("nome", claims.getBody().get("nome"));
			Optional<Cliente> c = clienteService.findByEmail(claims.getBody().getSubject().toString());
			mv.addObject("cliente", c.get());
			Set<Prestiti> listaPrestiti = c.get().getPrestiti();
			List<RichiestePrestito> listaRichiestePrestito = richiestePrestitoService
					.findByCodCliente(c.get().getCodCliente());

			mv.addObject("listaPrestiti", listaPrestiti);
			mv.addObject("prestito", prestito.get());

			mv.addObject("listaRichiestePrestito", listaRichiestePrestito);

			return mv;
		}
		return mv;
	}

	@PostMapping(value = "/richiediprestito")
	public ModelAndView richiediPrestito(@RequestParam("importo") double importo, @RequestParam("durata") int durataInMesi,
			@CookieValue(name = "token", required = false) String token, HttpServletRequest request) {
		Jws<Claims> claims = JWT.validate(token);

		Optional<Cliente> c = clienteService.findByEmail(claims.getBody().getSubject().toString());
		Prestiti prestito = new Prestiti();
		prestito.setCodCliente(c.get().getCodCliente());
		prestito.setDurataInMesi(durataInMesi);
		prestito.setTassoInteresse(0.22);
		prestito.setImporto(importo);
		prestitiService.savePrestiti(prestito);
		
		RichiestePrestito richiesta = new RichiestePrestito();
		richiesta.setCodCliente(c.get().getCodCliente());
		richiesta.setDataRichiesta(new Date());
		richiesta.setImporto(importo);
		richiesta.setStato(StatoPrestito.IN_ATTESA);
		richiestePrestitoService.saveRichiestePrestito(richiesta);
		return new ModelAndView("redirect:/visualizzaprestiti");
	}

}