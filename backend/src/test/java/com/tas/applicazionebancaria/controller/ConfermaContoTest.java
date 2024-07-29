package com.tas.applicazionebancaria.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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
class ConfermaContoTest {

	@Autowired
	MockMvc mvc;

	@Autowired
	ClienteService cls;
	@Autowired
	ContoService cos;
	
	static String token=JWT.generate("Nome", "Cognome", "Email");
	static Cliente c=new Cliente();

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		Set<Conto> conti=new HashSet<Conto>();
		Conto conto1=new Conto();
		conto1.setCodCliente(1);
		conto1.setCodConto(1);
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
	void clienteIsNotPresent() throws Exception {
		mvc.perform(post("/confermaconto")
				.param("tipoConto", TipoConto.CORRENTE.toString())
				.param("token", token)
				.cookie(new Cookie("token", token)))
			.andExpect(status().is(302));
	}
	
	@Test
	@Transactional
	void clienteNotPresent() throws Exception {
		c=cls.saveCliente(c);
		mvc.perform(post("/confermaconto")
				.param("tipoConto", TipoConto.CORRENTE.toString())
				.param("token", token)
				.cookie(new Cookie("token", token)))
			.andExpect(status().is(302));
		cls.deleteCliente(c);
		c.getConti().forEach(conto-> cos.deleteConto(conto));
	}

}
