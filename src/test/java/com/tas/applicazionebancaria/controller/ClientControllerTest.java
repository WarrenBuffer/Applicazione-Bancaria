package com.tas.applicazionebancaria.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.tas.applicazionebancaria.businesscomponent.model.Cliente;
import com.tas.applicazionebancaria.service.ClienteService;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(
		locations = "classpath:test.properties")
class ClientControllerTest {
	@Autowired
	ClienteService cs;

	@Test
	@Transactional
	void testVisualizzaConto() {
		Cliente c=new Cliente();
		c.setAccountBloccato(true);
		c.setCarte(null);
		c.setCodCliente(1);
		c.setCognomeCliente("Matteo");
		c.setConti(null);
		c.setEmailCliente("sas");
		c.setNomeCliente("marco");
		c.setPagamenti(null);
		c.setPasswordCliente("sas");
		c.setPrestiti(null);
		c.setTentativiErrati(1);
		
		cs.saveCliente(c);
		cs.findAll().forEach(System.out::println);
		assertTrue(cs.findById(1).isPresent());
	}

}
