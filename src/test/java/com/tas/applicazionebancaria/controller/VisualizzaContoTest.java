package com.tas.applicazionebancaria.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
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
import com.tas.applicazionebancaria.businesscomponent.model.Transazioni;
import com.tas.applicazionebancaria.businesscomponent.model.TransazioniBancarie;
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
	static Conto conto=new Conto();
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		Set<Conto> conti=new HashSet<Conto>();
		conto.setCodConto(1);
		conto.setSaldo(1001);
		conto.setTipoConto(TipoConto.CORRENTE);
		conto.setTransazioni(null);
		conto.setTransazioniBancarie(null);
		conti.add(conto);
		
		c.setAccountBloccato(true);
		c.setCarte(null);
		c.setCognomeCliente("Cognome");
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
	void listaContiEmpty() throws Exception {
		c.setConti(new HashSet<Conto>());
		Cliente c1=cls.saveCliente(c);
		mvc.perform(get("/visualizzaconti")
				.cookie(new Cookie("token", token)))
			.andExpect(status().isOk())
			.andExpect(view().name("visualizzaconti"));
		cls.deleteCliente(c1);
	}
	@Test
	@Order(3)
	void listaContiNotNull() throws Exception {
		Set<Conto> conti=new HashSet<Conto>();
		conto.setTransazioni(new HashSet<Transazioni>());
		conto.setTransazioniBancarie(new HashSet<TransazioniBancarie>());
		conti.add(conto);
		c.setConti(conti);
		Cliente c1=cls.saveCliente(c);
		conto.setCodCliente(c1.getCodCliente());
		conto=cos.saveConto(conto);
		c1.setConti(conti);
		mvc.perform(get("/visualizzaconti")
				.cookie(new Cookie("token", token)))
		.andExpect(status().isOk())
		.andExpect(view().name("visualizzaconti"))
		.andExpect(model().attributeExists("listaConti"));
		cls.deleteCliente(c1);
		cos.findAll().forEach(con->cos.deleteConto(con));
	}
	
}
