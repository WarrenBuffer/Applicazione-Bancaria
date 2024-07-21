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

import com.tas.applicazionebancaria.businesscomponent.model.Cliente;
import com.tas.applicazionebancaria.businesscomponent.model.Conto;
import com.tas.applicazionebancaria.config.BCryptEncoder;
import com.tas.applicazionebancaria.service.AmministratoreService;
import com.tas.applicazionebancaria.service.CarteDiCreditoService;
import com.tas.applicazionebancaria.service.ClienteService;
import com.tas.applicazionebancaria.service.ContoService;
import com.tas.applicazionebancaria.service.PagamentiService;
import com.tas.applicazionebancaria.service.PrestitiService;
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
	
	private static boolean validateInputs(String nome, String cognome, String email, String password) {
		if (nome == null || cognome == null || email == null || password == null) return false;
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
		return new ServerResponse(0, clienti);
	}

	@PostMapping("/clienti")
	public ServerResponse addCliente(
			@RequestBody Cliente cliente) {
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
		System.out.println("Sono Qui 2");
		stat.setTransazioniMediePerCliente(tmService.transazioniMediePerCliente());
		System.out.println("Sono Qui 3");
		stat.setImportoTransazioniPerMese(tmService.importoTransazioniPerMese());
		stat.setContiSaldo0(contoService.findConti0());
		return new ServerResponse(0, stat);
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
