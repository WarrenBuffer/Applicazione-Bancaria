package com.tas.applicazionebancaria.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.tas.applicazionebancaria.businesscomponent.model.Amministratore;
import com.tas.applicazionebancaria.businesscomponent.model.Cliente;
import com.tas.applicazionebancaria.config.BCryptEncoder;
import com.tas.applicazionebancaria.service.AmministratoreService;
import com.tas.applicazionebancaria.service.ClienteService;

@TestInstance(Lifecycle.PER_CLASS)
@SpringBootTest
class AccountBlockerTest {
	@Autowired
	AmministratoreService as;
	@Autowired
	ClienteService cs;
	@Autowired
	AccountBlocker accountBlocker;
	private static Amministratore admin;
	private static Cliente cliente;
	
	@BeforeAll
	void setup() {
		Amministratore a = new Amministratore();
		a.setNomeAdmin("Test");
		a.setCognomeAdmin("Test");
		a.setEmailAdmin("admin.test@gmail.com");
		a.setPasswordAdmin(BCryptEncoder.encode("Password01$"));
		admin = as.saveAmministratore(a);
		
		Cliente c = new Cliente();
		c.setNomeCliente("Test");
		c.setCognomeCliente("Test");
		c.setEmailCliente("client.test@gmail.com");
		c.setPasswordCliente(BCryptEncoder.encode("Password01$"));
		cliente = cs.saveCliente(c);
	}
	
	@AfterAll
	void tearDown() {
		as.deleteAmministratore(admin);
		cs.deleteCliente(cliente);
	}
	
	@Test
	void testInvalidClientInvalidEmail() {
		accountBlocker.invalidClient("sajdosaijdoiasjdoiajs");			
	}
	
	@Test
	void testInvalidClient() {
		for (int i = 0; i < 5; ++i) {
			accountBlocker.invalidClient(cliente.getEmailCliente());			
		}
		
		Cliente tmp = cs.findByEmail(cliente.getEmailCliente()).get();
		assertTrue(tmp.isAccountBloccato());
	}

	@Test
	void testInvalidAdminInvalidEmail() {
		accountBlocker.invalidAdmin("sajdosaijdoiasjdoiajs");			
	}
	
	@Test
	void testInvalidAdmin() {
		for (int i = 0; i < 5; ++i) {
			accountBlocker.invalidAdmin(admin.getEmailAdmin());			
		}
		
		Amministratore tmp = as.findByEmail(admin.getEmailAdmin()).get();
		assertTrue(tmp.isAccountBloccato());
	}
	
	@Test
	void testValidClientInvalidEmail() {
		accountBlocker.validClient("sajdosaijdoiasjdoiajs");			
	}
	
	@Test
	void testValidClient() {
		accountBlocker.validClient(cliente.getEmailCliente());			
		Cliente tmp = cs.findByEmail(cliente.getEmailCliente()).get();
		assertFalse(tmp.isAccountBloccato());
		assertEquals(tmp.getTentativiErrati(), 0);
	}
	
	@Test
	void testValidAdminInvalidEmail() {
		accountBlocker.validAdmin("sajdosaijdoiasjdoiajs");			
	}
	
	@Test
	void testValidAdmin() {
		accountBlocker.validAdmin(admin.getEmailAdmin());			
		Amministratore tmp = as.findByEmail(admin.getEmailAdmin()).get();
		assertFalse(tmp.isAccountBloccato());
		assertEquals(tmp.getTentativiErrati(), 0);
	}

}
