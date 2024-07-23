package com.tas.applicazionebancaria.restcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tas.applicazionebancaria.businesscomponent.model.Amministratore;
import com.tas.applicazionebancaria.businesscomponent.model.Cliente;
import com.tas.applicazionebancaria.businesscomponent.model.Conto;
import com.tas.applicazionebancaria.businesscomponent.model.RichiestePrestito;
import com.tas.applicazionebancaria.businesscomponent.model.enumerations.StatoPrestito;
import com.tas.applicazionebancaria.config.BCryptEncoder;
import com.tas.applicazionebancaria.service.AmministratoreService;
import com.tas.applicazionebancaria.service.CarteDiCreditoService;
import com.tas.applicazionebancaria.service.ClienteService;
import com.tas.applicazionebancaria.service.ContoService;
import com.tas.applicazionebancaria.service.PagamentiService;
import com.tas.applicazionebancaria.service.PrestitiService;
import com.tas.applicazionebancaria.service.RichiestePrestitoService;
import com.tas.applicazionebancaria.service.TransazioniBancarieService;
import com.tas.applicazionebancaria.service.TransazioniMongoService;
import com.tas.applicazionebancaria.service.TransazioniService;
import com.tas.applicazionebancaria.utils.ServerResponse;
import com.tas.applicazionebancaria.utils.Statistiche;

@RequestMapping(value = "/api")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
public class AdminController {
	@Autowired
	AmministratoreService adminService;
	@Autowired
	ClienteService clienteService;
	@Autowired
	ContoService contoService;
	@Autowired
	TransazioniService transazioniService;
	@Autowired
	TransazioniBancarieService transazioniBancarieService;
	@Autowired
	CarteDiCreditoService ccService;
	@Autowired
	PrestitiService prestitiService;
	@Autowired
	PagamentiService pagamentiService;
	@Autowired
	TransazioniMongoService tmService;
	@Autowired
	RichiestePrestitoService rpService;
	@Autowired
	AmministratoreService asService;

	private static boolean validateInputs(String nome, String cognome, String email, String password) {
		if (nome == null || cognome == null || email == null || password == null)
			return false;
		if (!nome.matches("^[a-zA-Z ,.'-]{2,30}$"))
			return false;
		if (!cognome.matches("^[a-zA-Z ,.'-]{2,30}$"))
			return false;
		if (!email.matches("^[\\w.%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"))
			return false;
		if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#&%^$?!=])[a-zA-Z0-9@#&%^$?!=]{7,15}$"))
			return false;
		return true;
	}

	@GetMapping("/clienti")
	public ServerResponse getClienti() {
		List<Cliente> clienti = clienteService.findAll();
		for (Cliente c : clienti) {
			System.out.println(c.getConti());
		}
		return new ServerResponse(0, clienti);
	}
	@GetMapping("/conti")
	public ServerResponse getConti() {
		List<Conto> conti = contoService.findConti0();
		return new ServerResponse(0, conti);
	}
	@PostMapping("/clienti")
	public ServerResponse addCliente(@RequestBody Cliente cliente) {
		System.out.println(cliente);
		if (!validateInputs(cliente.getNomeCliente(), cliente.getCognomeCliente(), cliente.getEmailCliente(),
				cliente.getPasswordCliente()))
			return new ServerResponse(1, "Validazione fallita, riprova a inserire i dati.");

		if (clienteService.findByEmail(cliente.getEmailCliente()).isPresent())
			return new ServerResponse(1, "Cliente con email " + cliente.getEmailCliente() + " esiste.");

		cliente.setPasswordCliente(BCryptEncoder.encode(cliente.getPasswordCliente()));
		clienteService.saveCliente(cliente);
		return new ServerResponse(0, "Cliente aggiunto con successo.");
	}

	@GetMapping("/clienti/{id}")
	public ServerResponse getClienteById(@PathVariable long id) {
		Optional<Cliente> cliente = clienteService.findById(id);
		if (cliente.isEmpty())
			return new ServerResponse(1, "Il cliente con id " + id + " non esiste.");

		return new ServerResponse(0, cliente.get());
	}
	@GetMapping("/clienti/email/{email}")
	public ServerResponse getClienteByEmail(@PathVariable String email) {
		Optional<Cliente> cliente = clienteService.findByEmail(email);
		if (cliente.isEmpty()) {
			return new ServerResponse(1, "Il cliente con email " + email + " non esiste.");
		}
		return new ServerResponse(0, cliente.get());
	}
	@PostMapping("/clienti/lock")
	public ServerResponse lockUnlock(@RequestBody String emailCliente) {
		Optional<Cliente> cliente = clienteService.findByEmail(emailCliente);
		System.out.println("start lockUnlock "+ emailCliente);
		if (cliente.isEmpty()) {
			return new ServerResponse(1, "Il cliente con email " + emailCliente + " non esiste.");
		}
		
		Cliente c=cliente.get();
		System.out.println(c);
		if(c.isAccountBloccato()) {
			c.setAccountBloccato(false);
		}else {
			c.setAccountBloccato(true);
		}
		clienteService.saveCliente(c);
		System.out.println(c);
		return new ServerResponse(0,"Cliente" + emailCliente + " modificato con successo.");
	}
	
	@DeleteMapping("/conti/{id}")
	public ServerResponse deleteContoById(@PathVariable long id) {
		Optional<Conto> conto = contoService.findById(id);
		if (conto.isEmpty())
			return new ServerResponse(1, "Il conto con id " + id + " non esiste.");

		contoService.deleteConto(conto.get());
		return new ServerResponse(0, "Conto " + id + " eliminato con successo.");
	}

	@GetMapping("/statistiche")
	public ServerResponse getStatistiche() {
		Statistiche stat = new Statistiche();
		stat.setNumeroClienti(clienteService.count());
		stat.setSaldoPiuAlto(clienteService.findClienteSaldoPiuAlto());
		stat.setUltimaTransazione(transazioniBancarieService.findUltimaTransazione());
		stat.setNumTransazioni(transazioniBancarieService.findNumTransazioni());
		stat.setSommaImporti(transazioniBancarieService.findSommaImporti());
		stat.setSaldoMedio(contoService.findSaldoMedio());
		stat.setContiPerCliente(findNumContiPerCliente());
		stat.setCartePerCliente(findNumCartePerCliente());
		stat.setPrestitiPerCliente(findTotPrestitiPerCliente());
		stat.setPagamentiPerCliente(findTotPagamentiPerCliente());
		stat.setTransazioniPerTipo(tmService.findTransazioniPerTipo());
		stat.setTransazioniMediePerCliente(tmService.transazioniMediePerCliente());
		stat.setImportoTransazioniPerMese(tmService.importoTransazioniPerMese());
		stat.setContiSaldo0(contoService.findConti0());
		return new ServerResponse(0, stat);
	}


	@GetMapping("/richiestePrestito")
	public ServerResponse richiestePrestito() {
		List<RichiestePrestito> richiestaPrestiti = rpService.findAll();
		if (richiestaPrestiti.isEmpty()) {
			return new ServerResponse(1, "Nessuna richiesta prestito pendente");
		}
		return new ServerResponse(0, richiestaPrestiti);
	}

	@GetMapping("/approvaPrestito/{id}")
	public ServerResponse approvaPrestito(@PathVariable long id) {
		System.out.println("approvato prestito");
		Optional<RichiestePrestito> rp = rpService.findById(id);
		if (rp.isEmpty()) {
			return new ServerResponse(1, "Richiesta Prestito non valida");
		}
		rp.get().setStato(StatoPrestito.APPROVATO);
		// salvo il prestito confermato nella tabella
		// Prestiti prestito = new Prestiti();
		// prestitiService.savePrestiti()
		rpService.saveRichiestePrestito(rp.get());
		
		return new ServerResponse(0, "Approvata richiesta n. " + rp.get().getCodRichiesta());
	}

	@GetMapping("/declinaPrestito/{id}")
	public ServerResponse declinaPrestito(@PathVariable long id) {
		Optional<RichiestePrestito> rp = rpService.findById(id);
		if (rp.isEmpty()) {
			return new ServerResponse(1, "Richiesta Prestito non valida");
		}
		rp.get().setStato(StatoPrestito.RIFIUTATO);
		rpService.saveRichiestePrestito(rp.get());
		return new ServerResponse(0, "Rifiutata richiesta n. " + rp.get().getCodRichiesta());
	}
	
	@GetMapping("/confermaNuovaPassword/{email}/{password}")
	public ServerResponse confermaNuovaPassword(@PathVariable String email, @PathVariable String password) {
		Optional<Amministratore> a = asService.findByEmail(email);
		if (a.isEmpty()) {
			return new ServerResponse(1, "Amministratore non trovato");
		}
		a.get().setPasswordAdmin(BCryptEncoder.encode(password));
		asService.saveAmministratore(a.get());
		return new ServerResponse(0, "Password salvata correttamente");
	}


	private Map<Cliente, Long> findNumContiPerCliente() {
		Map<Cliente, Long> numContiPerCliente = new HashMap<Cliente, Long>();
		for (Cliente c : clienteService.findAll()) {
			long numConti = contoService.findNumContiByCodCliente(c.getCodCliente());
			numContiPerCliente.put(c, numConti);
		}
		return numContiPerCliente;
	}

	private Map<Cliente, Long> findNumCartePerCliente() {
		Map<Cliente, Long> numCartePerCliente = new HashMap<Cliente, Long>();
		for (Cliente c : clienteService.findAll()) {
			long numCarte = ccService.findNumCarteByCodCliente(c.getCodCliente());
			numCartePerCliente.put(c, numCarte);
		}
		return numCartePerCliente;
	}

	private Map<Cliente, Double> findTotPrestitiPerCliente() {
		Map<Cliente, Double> totPrestitiPerCliente = new HashMap<Cliente, Double>();
		for (Cliente c : clienteService.findAll()) {
			double totPrestiti = prestitiService.findTotPrestitiByCodCliente(c.getCodCliente());
			totPrestitiPerCliente.put(c, totPrestiti);
		}
		return totPrestitiPerCliente;
	}

	private Map<Cliente, Double> findTotPagamentiPerCliente() {
		Map<Cliente, Double> totPagamentiPerCliente = new HashMap<Cliente, Double>();
		for (Cliente c : clienteService.findAll()) {
			double totPagamenti = pagamentiService.findTotPagamentiByCodCliente(c.getCodCliente());
			totPagamentiPerCliente.put(c, totPagamenti);
		}
		return totPagamentiPerCliente;
	}

}
