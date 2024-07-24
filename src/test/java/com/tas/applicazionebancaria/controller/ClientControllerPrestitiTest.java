package com.tas.applicazionebancaria.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.tas.applicazionebancaria.businesscomponent.model.Cliente;
import com.tas.applicazionebancaria.businesscomponent.model.Prestiti;
import com.tas.applicazionebancaria.businesscomponent.model.RichiestePrestito;
import com.tas.applicazionebancaria.businesscomponent.model.enumerations.StatoPrestito;
import com.tas.applicazionebancaria.config.BCryptEncoder;
import com.tas.applicazionebancaria.service.ClienteService;
import com.tas.applicazionebancaria.service.PrestitiService;
import com.tas.applicazionebancaria.service.RichiestePrestitoService;
import com.tas.applicazionebancaria.utils.JWT;

import jakarta.servlet.http.Cookie;

@TestInstance(Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@SpringBootTest
class ClientControllerPrestitiTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	ClienteService cs;
	@Autowired
	PrestitiService ps;
	@Autowired
	RichiestePrestitoService rps;
	
	private Cliente cliente;
	private Prestiti prestiti;
	private RichiestePrestito richiestePrestito;
	
	@BeforeAll
	void setup() {
		Cliente c = new Cliente();
		c.setNomeCliente("Test");
		c.setCognomeCliente("Test");
		c.setEmailCliente("test@test.com");
		c.setPasswordCliente(BCryptEncoder.encode("Password01$"));
		cliente = cs.saveCliente(c);
		
		Prestiti p = new Prestiti();
		p.setCodCliente(cliente.getCodCliente());
		p.setDurataInMesi(10);
		p.setImporto(3000.0);
		p.setTassoInteresse(3.2);
		prestiti = ps.savePrestiti(p);
		
		RichiestePrestito rp = new RichiestePrestito();
		rp.setCodCliente(cliente.getCodCliente());
		rp.setDataRichiesta(new Date());
		rp.setImporto(prestiti.getImporto());
		rp.setStato(StatoPrestito.IN_ATTESA);
		richiestePrestito = rps.saveRichiestePrestito(rp);
	}
	
	@AfterAll
	void tearDown() {
		List<RichiestePrestito> lrp = rps.findByCodCliente(cliente.getCodCliente());
		for (RichiestePrestito rp : lrp) {
			rps.deleteRichiestePrestito(rp);			
		}
		ps.deletePrestiti(prestiti);
		cs.deleteCliente(cliente);
	}
	
	@Test
	void testVisualizzaPrestiti() throws Exception {
		Cookie cookie = new Cookie("token", JWT.generate(cliente.getNomeCliente(), cliente.getCognomeCliente(), cliente.getEmailCliente()));
		ResultActions result = mockMvc.perform(get("/visualizzaprestiti").cookie(cookie));
		result
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("nome"))
			.andExpect(model().attributeExists("listaPrestiti"))
			.andExpect(model().attributeExists("listaRichiestePrestito"))
			.andExpect(view().name("visualizzaprestiti"));
	}
	
	@Test
	void testVisualizzaPrestitiNoAttributes() throws Exception {
		rps.deleteRichiestePrestito(richiestePrestito);
		ps.deletePrestiti(prestiti);
		Cookie cookie = new Cookie("token", JWT.generate(cliente.getNomeCliente(), cliente.getCognomeCliente(), cliente.getEmailCliente()));
		ResultActions result = mockMvc.perform(get("/visualizzaprestiti").cookie(cookie));
		result
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("nome"))
			.andExpect(model().attributeDoesNotExist("listaPrestiti"))
			.andExpect(model().attributeDoesNotExist("listaRichiestePrestito"))
			.andExpect(view().name("visualizzaprestiti"));
		
		prestiti = ps.savePrestiti(prestiti);
		richiestePrestito = rps.saveRichiestePrestito(richiestePrestito);
	}
	
	@Test
	void testSelPrestito() throws Exception {
		Cookie cookie = new Cookie("token", JWT.generate(cliente.getNomeCliente(), cliente.getCognomeCliente(), cliente.getEmailCliente()));
		ResultActions result = mockMvc.perform(post("/selprestito").cookie(cookie).param("codPrestito", String.valueOf(prestiti.getCodPrestito())));
		result
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("nome"))
			.andExpect(model().attributeExists("cliente"))
			.andExpect(model().attributeExists("listaPrestiti"))
			.andExpect(model().attributeExists("prestito"))
			.andExpect(model().attributeExists("listaRichiestePrestito"))
			.andExpect(view().name("visualizzaprestiti"));
	}
	
	@Test
	void testSelPrestitoNoParam() throws Exception {
		Cookie cookie = new Cookie("token", JWT.generate(cliente.getNomeCliente(), cliente.getCognomeCliente(), cliente.getEmailCliente()));
		ResultActions result = mockMvc.perform(post("/selprestito").cookie(cookie));
		result
		.andExpect(status().is4xxClientError());
	}
	
	@Test
	void testSelPrestitoEmptyLists() throws Exception {
		rps.deleteRichiestePrestito(richiestePrestito);
		ps.deletePrestiti(prestiti);
		Cookie cookie = new Cookie("token", JWT.generate(cliente.getNomeCliente(), cliente.getCognomeCliente(), cliente.getEmailCliente()));
		ResultActions result = mockMvc.perform(post("/selprestito").cookie(cookie).param("codPrestito", String.valueOf(prestiti.getCodPrestito())));
		result
			.andExpect(status().isOk())
			.andExpect(model().attributeDoesNotExist("nome"))
			.andExpect(model().attributeDoesNotExist("cliente"))
			.andExpect(model().attributeDoesNotExist("listaPrestiti"))
			.andExpect(model().attributeDoesNotExist("prestito"))
			.andExpect(model().attributeDoesNotExist("listaRichiestePrestito"))
			.andExpect(view().name("visualizzaprestiti"));
		
		prestiti = ps.savePrestiti(prestiti);
		richiestePrestito = rps.saveRichiestePrestito(richiestePrestito);
	}
	
	@Test
	void testRichiediPrestito() throws Exception {
		Cookie cookie = new Cookie("token", JWT.generate(cliente.getNomeCliente(), cliente.getCognomeCliente(), cliente.getEmailCliente()));
		ResultActions result = mockMvc.perform(post("/richiediprestito").cookie(cookie).param("importo", String.valueOf(10000)));
		result.andExpect(status().is3xxRedirection());
	}

}
