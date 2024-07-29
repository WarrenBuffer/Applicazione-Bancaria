package com.tas.applicazionebancaria.utils;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tas.applicazionebancaria.businesscomponent.model.Amministratore;
import com.tas.applicazionebancaria.businesscomponent.model.Cliente;
import com.tas.applicazionebancaria.service.AmministratoreService;
import com.tas.applicazionebancaria.service.ClienteService;


@Component
public class AccountBlocker implements Costanti{
	@Autowired
	AmministratoreService as;
	@Autowired
	ClienteService cs;
	@Autowired
	EmailService es;
	
	public void invalidClient(String email) {
		Optional<Cliente> c = cs.findByEmail(email);
		if (c.isEmpty()) return;
		
		Cliente cliente = c.get();
		int tentativi = cliente.getTentativiErrati() + 1;
		cliente.setTentativiErrati(tentativi);
		
		if (tentativi >= MAX_TENTATIVI) {
			cliente.setAccountBloccato(true);	
			es.sendEmail(email, "Account bloccato.", "Hai raggiunto il numero massimo di tentativi, il tuo account è stato bloccato.\nContatta l'amministratore.");
		} 
		
		cs.saveCliente(cliente);
		
	}
	
	public void invalidAdmin(String email) {
		Optional<Amministratore> a = as.findByEmail(email);
		if (a.isEmpty()) return;
		
		Amministratore admin = a.get();
		int tentativi = admin.getTentativiErrati() + 1;
		admin.setTentativiErrati(tentativi);
		
		if (tentativi >= MAX_TENTATIVI) {
			admin.setAccountBloccato(true);
			es.sendEmail(email, "Account bloccato.", "Hai raggiunto il numero massimo di tentativi, il tuo account è stato bloccato.\nContatta l'amministratore.");
		} 
		
		as.saveAmministratore(admin);
	}
	
	public void validClient(String email) {
		Optional<Cliente> c = cs.findByEmail(email);
		if (c.isEmpty()) return;
		
		Cliente cliente = c.get();
		cliente.setTentativiErrati(0);
		cliente.setAccountBloccato(false);
		
		cs.saveCliente(cliente);
	}
	
	public void validAdmin(String email) {
		Optional<Amministratore> a = as.findByEmail(email);
		if (a.isEmpty()) return;
		
		Amministratore admin = a.get();
		admin.setTentativiErrati(0);
		admin.setAccountBloccato(false);
		
		as.saveAmministratore(admin);
	}
}
