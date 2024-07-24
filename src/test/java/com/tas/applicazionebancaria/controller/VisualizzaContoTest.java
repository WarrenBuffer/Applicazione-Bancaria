package com.tas.applicazionebancaria.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.tas.applicazionebancaria.businesscomponent.model.Cliente;
import com.tas.applicazionebancaria.businesscomponent.model.Conto;
import com.tas.applicazionebancaria.businesscomponent.model.enumerations.TipoConto;
import com.tas.applicazionebancaria.service.ClienteService;
import com.tas.applicazionebancaria.service.ContoService;
import com.tas.applicazionebancaria.utils.JWT;

import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(
		locations = "classpath:test.properties")
@TestMethodOrder(OrderAnnotation.class)
class VisualizzaContoTest {
	@Autowired
	MockMvc mvc;
	static String token=JWT.generate("Nome", "Cognome", "Email");

	@Autowired
	ClienteService cls;
	@Autowired
	ContoService cos;
	
	static Cliente c=new Cliente();
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		
		Set<Conto> conti=new HashSet<Conto>();
		Conto conto1=new Conto();
		conto1.setCodCliente(1);
		conto1.setCodConto(1);
		conto1.setEmailCliente("emailCliente");
		conto1.setSaldo(1000);
		conto1.setTipoConto(TipoConto.CORRENTE);
		conto1.setTransazioni(null);
		conto1.setTransazioniBancarie(null);
		conti.add(conto1);
		
		c.setAccountBloccato(true);
		c.setCarte(null);
		c.setCodCliente(1);
		c.setCognomeCliente("Cognome");
		c.setConti(conti);
		c.setEmailCliente("Email");
		c.setNomeCliente("Nome");
		c.setPagamenti(null);
		c.setPasswordCliente("Password");
		c.setPrestiti(null);
		c.setTentativiErrati(1);
	}

	@Test
	@Transactional
	@Order(1)
	void senzaCookie() throws Exception {
		mvc.perform(get("/visualizzaconti")
				.cookie(new Cookie("token", "test")))
			.andExpect(status().is(302));
	}
	
	@Test
	@Transactional
	@Order(2)
	void listaContiNotNull() throws Exception {
		cls.saveCliente(c);
		mvc.perform(get("/visualizzaconti")
				.cookie(new Cookie("token", token)))
		.andExpect(status().isOk())
		.andExpect(view().name("visualizzaconti"))
		.andExpect(model().attribute("listaConti", cos.findByIdCliente(c.getCodCliente())));
	}
	@Test
	@Transactional
	@Order(3)
	void listaContiNull() throws Exception {
		c.setConti(new HashSet<Conto>());
		cls.saveCliente(c);
		mvc.perform(get("/visualizzaconti")
				.cookie(new Cookie("token", token)))
			.andExpect(status().isOk())
			.andExpect(view().name("visualizzaconti"));
		
		c.setConti(null);
		cls.saveCliente(c);
		mvc.perform(get("/visualizzaconti")
				.cookie(new Cookie("token", token)))
			.andExpect(status().isOk())
			.andExpect(view().name("visualizzaconti"));
		
	}

}
