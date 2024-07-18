package com.tas.applicazionebancaria.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
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
import com.tas.applicazionebancaria.service.AmministratoreService;
import com.tas.applicazionebancaria.service.ClienteService;
import com.tas.applicazionebancaria.service.ContoService;
import com.tas.applicazionebancaria.utils.JWT;
import com.tas.applicazionebancaria.utils.ServerResponse;

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

	private static boolean validateInputs(String nome, String cognome, String email, String password) {
		if (!nome.matches("^[a-zA-Z ,.'-]{2,30}$"))
			return false;
		if (!cognome.matches("^[a-zA-Z ,.'-]{2,30}$"))
			return false;
		if (!email.matches("^[\\w.%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"))
			return false;
		if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#&%^$?=])[a-zA-Z0-9@#&%^$?=]{7,15}$"))
			return false;
		return true;
	}

	@GetMapping("/clienti")
	public ServerResponse getClienti(@CookieValue(name = "token", required = true) String jwt) {
		try {
			JWT.validate(jwt);
		} catch (Exception exc) {
			return new ServerResponse(-1, "JWT invalido. Rieffettua il login.");
		}

		List<Cliente> clienti = clienteService.findAll();
		return new ServerResponse(0, clienti);
	}

	@PostMapping("/clienti")
	public ServerResponse addCliente(@CookieValue(name = "token", required = true) String jwt,
			@RequestBody Cliente cliente) {
		try {
			JWT.validate(jwt);
		} catch (Exception exc) {
			return new ServerResponse(-1, "JWT invalido. Rieffettua il login.");
		}
		if (!validateInputs(cliente.getNomeCliente(), cliente.getCognomeCliente(), cliente.getEmailCliente(),
				cliente.getPasswordCliente()))
			return new ServerResponse(1, "Validazione fallita, riprova a inserire i dati.");

		if (clienteService.findByEmail(cliente.getEmailCliente()).isPresent())
			return new ServerResponse(1, "Cliente con email " + cliente.getEmailCliente() + " esiste.");

		clienteService.saveCliente(cliente);
		return new ServerResponse(0, "Cliente aggiunto con successo.");
	}

	@GetMapping("/clienti/{id}")
	public ServerResponse getClienteById(@CookieValue(name = "token", required = true) String jwt,
			@PathVariable long id) {
		try {
			JWT.validate(jwt);
		} catch (Exception exc) {
			return new ServerResponse(-1, "JWT invalido. Rieffettua il login.");
		}

		Optional<Cliente> cliente = clienteService.findById(id);
		if (cliente.isEmpty())
			return new ServerResponse(1, "Il cliente con id " + id + " non esiste.");

		return new ServerResponse(0, cliente.get());
	}

	@DeleteMapping("/conti/{id}")
	public ServerResponse deleteContoById(@CookieValue(name = "token", required = true) String jwt,
			@PathVariable long id) {
		try {
			JWT.validate(jwt);
		} catch (Exception exc) {
			return new ServerResponse(-1, "JWT invalido. Rieffettua il login.");
		}

		Optional<Conto> conto = contoService.findById(id);
		if (conto.isEmpty())
			return new ServerResponse(1, "Il conto con id " + id + " non esiste.");

		contoService.deleteConto(conto.get());
		return new ServerResponse(0, "Conto " + id + " eliminato con successo.");
	}

	// TODO: statistiche, bisogna decidere quali statistiche fare
}
