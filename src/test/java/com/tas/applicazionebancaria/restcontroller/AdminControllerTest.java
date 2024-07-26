package com.tas.applicazionebancaria.restcontroller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tas.applicazionebancaria.businesscomponent.model.Amministratore;
import com.tas.applicazionebancaria.businesscomponent.model.Cliente;
import com.tas.applicazionebancaria.businesscomponent.model.Conto;
import com.tas.applicazionebancaria.businesscomponent.model.RichiestePrestito;
import com.tas.applicazionebancaria.businesscomponent.model.TransazioniMongo;
import com.tas.applicazionebancaria.businesscomponent.model.enumerations.StatoPrestito;
import com.tas.applicazionebancaria.businesscomponent.model.enumerations.TipoConto;
import com.tas.applicazionebancaria.service.AmministratoreService;
import com.tas.applicazionebancaria.service.ClienteService;
import com.tas.applicazionebancaria.service.ContoService;
import com.tas.applicazionebancaria.service.RichiestePrestitoService;
import com.tas.applicazionebancaria.service.TransazioniMongoService;
import com.tas.applicazionebancaria.utils.JWT;
import com.tas.applicazionebancaria.utils.LoginRequest;

import jakarta.servlet.http.Cookie;

@TestInstance(Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@TestPropertySource(locations = "classpath:test.properties")
class AdminControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	ClienteService cs;
	@MockBean
	ContoService cos;
	@MockBean
	RichiestePrestitoService rps;
	@MockBean
	TransazioniMongoService tmService;
	@Autowired
	AmministratoreService as;

	private static Conto conto;
	private static Cliente cliente;
	private static RichiestePrestito prestito;
	private static Amministratore admin;

	@BeforeAll
	void setup() {
		cliente = new Cliente();
		cliente.setNomeCliente("Test");
		cliente.setCognomeCliente("Test");
		cliente.setEmailCliente("testcliente123456789@testcliente123456789.com");
		cliente.setPasswordCliente("TestPassword01$");

		conto = new Conto();
		conto.setCodCliente(cliente.getCodCliente());
		conto.setSaldo(10000);
		conto.setTipoConto(TipoConto.CORRENTE);

		prestito = new RichiestePrestito();
		prestito.setCodCliente(cliente.getCodCliente());
		prestito.setDataRichiesta(new Date());
		prestito.setImporto(10000);
		prestito.setStato(StatoPrestito.IN_ATTESA);

		admin = new Amministratore();
		admin.setCodAdmin(1);
		admin.setNomeAdmin("Test");
		admin.setCognomeAdmin("Test");
		admin.setEmailAdmin("testadmin123456789@testadmin123456789.com");
		admin.setPasswordAdmin("TestPassword01$");
		admin = as.saveAmministratore(admin);
	}

	@AfterAll
	void tearDown() {
		as.deleteAmministratore(admin);
	}

	@Test
	void testClienti() throws Exception {
		Cookie cookie = new Cookie("bearer",
				JWT.generate(admin.getNomeAdmin(), admin.getCognomeAdmin(), admin.getEmailAdmin()));
		ResultActions result = mockMvc.perform(get("/api/clienti").cookie(cookie));
		result.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.code").value(0));
	}

	@Test
	void testContiSuccess() throws Exception {
		Cookie cookie = new Cookie("bearer",
				JWT.generate(admin.getNomeAdmin(), admin.getCognomeAdmin(), admin.getEmailAdmin()));
		ResultActions result = mockMvc.perform(get("/api/conti").cookie(cookie));
		result.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.code").value(0));
	}

	@Test
	void testAddCliente() throws Exception {
		when(cs.saveCliente(cliente)).thenReturn(cliente);
		Cookie cookie = new Cookie("bearer",
				JWT.generate(admin.getNomeAdmin(), admin.getCognomeAdmin(), admin.getEmailAdmin()));
		ResultActions result = mockMvc.perform(post("/api/clienti").cookie(cookie)
				.contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsBytes(cliente)));
		result.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.code").value(0));
	}

	@Test
	void testAddClienteInvalidEmail() throws Exception {
		when(cs.saveCliente(cliente)).thenReturn(cliente);
		Cookie cookie = new Cookie("bearer",
				JWT.generate(admin.getNomeAdmin(), admin.getCognomeAdmin(), admin.getEmailAdmin()));
		cliente.setEmailCliente("pierooaisoisajdoij");
		ResultActions result = mockMvc.perform(post("/api/clienti").cookie(cookie)
				.contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsBytes(cliente)));
		result.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.code").value(1));
		cliente.setEmailCliente("piero64@gmail.com");
	}

	@Test
	void testAddClienteNullInput() throws Exception {
		Cookie cookie = new Cookie("bearer",
				JWT.generate(admin.getNomeAdmin(), admin.getCognomeAdmin(), admin.getEmailAdmin()));

		cliente.setEmailCliente(null);
		ResultActions result = mockMvc.perform(post("/api/clienti").cookie(cookie)
				.contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsBytes(cliente)));
		result.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.code").value(1));
		cliente.setEmailCliente("piero64@gmail.com");

		cliente.setNomeCliente(null);
		result = mockMvc.perform(post("/api/clienti").cookie(cookie).contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsBytes(cliente)));
		result.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.code").value(1));
		cliente.setNomeCliente("Piero");

		cliente.setCognomeCliente(null);
		result = mockMvc.perform(post("/api/clienti").cookie(cookie).contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsBytes(cliente)));
		result.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.code").value(1));
		cliente.setCognomeCliente("Feltrin");

		cliente.setPasswordCliente(null);
		result = mockMvc.perform(post("/api/clienti").cookie(cookie).contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsBytes(cliente)));
		result.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.code").value(1));
		cliente.setPasswordCliente("Piero01$");
	}

	@Test
	void testAddClienteInvalidNome() throws Exception {
		when(cs.saveCliente(cliente)).thenReturn(cliente);
		Cookie cookie = new Cookie("bearer",
				JWT.generate(admin.getNomeAdmin(), admin.getCognomeAdmin(), admin.getEmailAdmin()));
		cliente.setNomeCliente("");
		ResultActions result = mockMvc.perform(post("/api/clienti").cookie(cookie)
				.contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsBytes(cliente)));
		result.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.code").value(1));
		cliente.setNomeCliente("Piero");
	}

	@Test
	void testAddClienteInvalidCognome() throws Exception {
		when(cs.saveCliente(cliente)).thenReturn(cliente);
		Cookie cookie = new Cookie("bearer",
				JWT.generate(admin.getNomeAdmin(), admin.getCognomeAdmin(), admin.getEmailAdmin()));
		cliente.setCognomeCliente("");
		ResultActions result = mockMvc.perform(post("/api/clienti").cookie(cookie)
				.contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsBytes(cliente)));
		result.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.code").value(1));
		cliente.setCognomeCliente("Feltrin");
	}

	@Test
	void testAddClienteInvalidPassword() throws Exception {
		when(cs.saveCliente(cliente)).thenReturn(cliente);
		Cookie cookie = new Cookie("bearer",
				JWT.generate(admin.getNomeAdmin(), admin.getCognomeAdmin(), admin.getEmailAdmin()));
		cliente.setPasswordCliente("lala");
		ResultActions result = mockMvc.perform(post("/api/clienti").cookie(cookie)
				.contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsBytes(cliente)));
		result.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.code").value(1));
		cliente.setPasswordCliente("Piero01$");
	}

	@Test
	void testAddClienteAlreadyPresent() throws Exception {
		when(cs.saveCliente(cliente)).thenReturn(cliente);
		when(cs.findByEmail(cliente.getEmailCliente())).thenReturn(Optional.of(cliente));
		Cookie cookie = new Cookie("bearer",
				JWT.generate(admin.getNomeAdmin(), admin.getCognomeAdmin(), admin.getEmailAdmin()));
		ResultActions result = mockMvc.perform(post("/api/clienti").cookie(cookie)
				.contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsBytes(cliente)));
		result.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.code").value(1));
	}

	@Test
	void testClienteById() throws Exception {
		when(cs.findById(cliente.getCodCliente())).thenReturn(Optional.of(cliente));
		Cookie cookie = new Cookie("bearer",
				JWT.generate(admin.getNomeAdmin(), admin.getCognomeAdmin(), admin.getEmailAdmin()));
		ResultActions result = mockMvc.perform(get("/api/clienti/" + cliente.getCodCliente()).cookie(cookie));
		result.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.code").value(0));
	}

	@Test
	void testClienteByIdNotPresent() throws Exception {
		when(cs.findById(cliente.getCodCliente())).thenReturn(Optional.empty());
		Cookie cookie = new Cookie("bearer",
				JWT.generate(admin.getNomeAdmin(), admin.getCognomeAdmin(), admin.getEmailAdmin()));
		ResultActions result = mockMvc.perform(get("/api/clienti/" + cliente.getCodCliente()).cookie(cookie));
		result.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.code").value(1));
	}

	@Test
	void testClienteByEmail() throws Exception {
		when(cs.findByEmail(cliente.getEmailCliente())).thenReturn(Optional.of(cliente));
		Cookie cookie = new Cookie("bearer",
				JWT.generate(admin.getNomeAdmin(), admin.getCognomeAdmin(), admin.getEmailAdmin()));
		ResultActions result = mockMvc.perform(get("/api/clienti/email/" + cliente.getEmailCliente()).cookie(cookie));
		result.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.code").value(0));
	}

	@Test
	void testClienteByEmailNotPresent() throws Exception {
		when(cs.findByEmail(cliente.getEmailCliente())).thenReturn(Optional.empty());
		Cookie cookie = new Cookie("bearer",
				JWT.generate(admin.getNomeAdmin(), admin.getCognomeAdmin(), admin.getEmailAdmin()));
		ResultActions result = mockMvc.perform(get("/api/clienti/email/" + cliente.getEmailCliente()).cookie(cookie));
		result.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.code").value(1));
	}

	@Test
	void testBlockCliente() throws Exception {
		when(cs.findByEmail(cliente.getEmailCliente())).thenReturn(Optional.of(cliente));
		Cookie cookie = new Cookie("bearer",
				JWT.generate(admin.getNomeAdmin(), admin.getCognomeAdmin(), admin.getEmailAdmin()));
		LoginRequest request = new LoginRequest();
		request.setEmail(cliente.getEmailCliente());
		ResultActions result = mockMvc.perform(post("/api/clienti/lock").cookie(cookie)
				.contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsBytes(request)));
		result.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.code").value(0));
	}

	@Test
	void testBlockClienteNotPresent() throws Exception {
		when(cs.findByEmail(cliente.getEmailCliente())).thenReturn(Optional.empty());

		Cookie cookie = new Cookie("bearer",
				JWT.generate(admin.getNomeAdmin(), admin.getCognomeAdmin(), admin.getEmailAdmin()));
		LoginRequest request = new LoginRequest();
		request.setEmail(cliente.getEmailCliente());

		ResultActions result = mockMvc.perform(post("/api/clienti/lock").cookie(cookie)
				.contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsBytes(request)));
		result.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.code").value(1));
	}

	@Test
	void testBlockClienteAlreadyBlocked() throws Exception {
		cliente.setAccountBloccato(true);
		when(cs.findByEmail(cliente.getEmailCliente())).thenReturn(Optional.of(cliente));
		cliente.setAccountBloccato(false);

		Cookie cookie = new Cookie("bearer",
				JWT.generate(admin.getNomeAdmin(), admin.getCognomeAdmin(), admin.getEmailAdmin()));
		LoginRequest request = new LoginRequest();
		request.setEmail(cliente.getEmailCliente());

		ResultActions result = mockMvc.perform(post("/api/clienti/lock").cookie(cookie)
				.contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsBytes(request)));
		result.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.code").value(0));
	}

	@Test
	void testDeleteConto() throws Exception {
		when(cos.findById(conto.getCodConto())).thenReturn(Optional.of(conto));

		Cookie cookie = new Cookie("bearer",
				JWT.generate(admin.getNomeAdmin(), admin.getCognomeAdmin(), admin.getEmailAdmin()));

		ResultActions result = mockMvc
				.perform(MockMvcRequestBuilders.delete("/api/conti/" + conto.getCodConto()).cookie(cookie));
		result.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.code").value(0));
	}

	@Test
	void testDeleteContoNotFound() throws Exception {
		when(cos.findById(conto.getCodConto())).thenReturn(Optional.empty());

		Cookie cookie = new Cookie("bearer",
				JWT.generate(admin.getNomeAdmin(), admin.getCognomeAdmin(), admin.getEmailAdmin()));

		ResultActions result = mockMvc
				.perform(MockMvcRequestBuilders.delete("/api/conti/" + conto.getCodConto()).cookie(cookie));
		result.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.code").value(1));
	}

	@Test
	void testStatistiche() throws Exception {
		Cookie cookie = new Cookie("bearer",
				JWT.generate(admin.getNomeAdmin(), admin.getCognomeAdmin(), admin.getEmailAdmin()));

		when(tmService.findTotAddebiti()).thenReturn(Optional.of(2l));
		when(tmService.findTotAccrediti()).thenReturn(Optional.of(2l));
		when(tmService.transazioniMediePerCliente()).thenReturn(Optional.of(2l));
		when(tmService.importoTransazioniPerMese()).thenReturn(Optional.of(List.of(new TransazioniMongo())));
		ResultActions result = mockMvc.perform(get("/api/statistiche").cookie(cookie));
		result.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.code").value(0));
	}

	@Test
	void testStatisticheNoValues() throws Exception {
		Cookie cookie = new Cookie("bearer",
				JWT.generate(admin.getNomeAdmin(), admin.getCognomeAdmin(), admin.getEmailAdmin()));

		when(tmService.findTotAddebiti()).thenReturn(Optional.empty());
		when(tmService.findTotAccrediti()).thenReturn(Optional.empty());
		when(tmService.transazioniMediePerCliente()).thenReturn(Optional.empty());
		when(tmService.importoTransazioniPerMese()).thenReturn(Optional.empty());

		ResultActions result = mockMvc.perform(get("/api/statistiche").cookie(cookie));
		result.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.code").value(0));
	}

	@Test
	void testRichiestePrestito() throws Exception {
		Cookie cookie = new Cookie("bearer",
				JWT.generate(admin.getNomeAdmin(), admin.getCognomeAdmin(), admin.getEmailAdmin()));
		ResultActions result = mockMvc.perform(get("/api/richiestePrestito").cookie(cookie));
		result.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.code").value(0));
	}

	@Test
	void testApprovaPrestito() throws Exception {
		when(rps.findById(prestito.getCodRichiesta())).thenReturn(Optional.of(prestito));
		Cookie cookie = new Cookie("bearer",
				JWT.generate(admin.getNomeAdmin(), admin.getCognomeAdmin(), admin.getEmailAdmin()));
		ResultActions result = mockMvc
				.perform(get("/api/approvaPrestito/" + prestito.getCodRichiesta()).cookie(cookie));
		result.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.code").value(0));
	}

	@Test
	void testApprovaPrestitoNotFound() throws Exception {
		when(rps.findById(prestito.getCodRichiesta())).thenReturn(Optional.empty());
		Cookie cookie = new Cookie("bearer",
				JWT.generate(admin.getNomeAdmin(), admin.getCognomeAdmin(), admin.getEmailAdmin()));
		ResultActions result = mockMvc
				.perform(get("/api/approvaPrestito/" + prestito.getCodRichiesta()).cookie(cookie));
		result.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.code").value(1));
	}

	@Test
	void testDeclinaPrestito() throws Exception {
		when(rps.findById(prestito.getCodRichiesta())).thenReturn(Optional.of(prestito));
		Cookie cookie = new Cookie("bearer",
				JWT.generate(admin.getNomeAdmin(), admin.getCognomeAdmin(), admin.getEmailAdmin()));
		ResultActions result = mockMvc
				.perform(get("/api/declinaPrestito/" + prestito.getCodRichiesta()).cookie(cookie));
		result.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.code").value(0));
	}

	@Test
	void testDeclinaPrestitoNotFound() throws Exception {
		when(rps.findById(prestito.getCodRichiesta())).thenReturn(Optional.empty());

		Cookie cookie = new Cookie("bearer",
				JWT.generate(admin.getNomeAdmin(), admin.getCognomeAdmin(), admin.getEmailAdmin()));

		ResultActions result = mockMvc
				.perform(get("/api/declinaPrestito/" + prestito.getCodRichiesta()).cookie(cookie));
		result.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.code").value(1));
	}

	@Test
	void testCambiaPassword() throws Exception {
		Cookie cookie = new Cookie("bearer",
				JWT.generate(admin.getNomeAdmin(), admin.getCognomeAdmin(), admin.getEmailAdmin()));
		LoginRequest request = new LoginRequest();
		request.setPassword("NewPassword01$");
		ResultActions result = mockMvc.perform(post("/api/confermaNuovaPassword").cookie(cookie)
				.contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsBytes(request)));
		result.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.code").value(0));
	}

	@Test
	void testGetTransazioni() throws Exception {
		Cookie cookie = new Cookie("bearer",
				JWT.generate(admin.getNomeAdmin(), admin.getCognomeAdmin(), admin.getEmailAdmin()));
		ResultActions result = mockMvc.perform(get("/api/transazioni").cookie(cookie));
		result.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.code").value(0));
	}

	@Test
	void testGetPrestiti() throws Exception {
		Cookie cookie = new Cookie("bearer",
				JWT.generate(admin.getNomeAdmin(), admin.getCognomeAdmin(), admin.getEmailAdmin()));
		ResultActions result = mockMvc.perform(get("/api/prestiti").cookie(cookie));
		result.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.code").value(0));
	}

	// ############## NO AUTH ##############
	@Test
	void testClientiNoAuth() throws Exception {
		ResultActions result = mockMvc.perform(get("/api/clienti"));
		result.andExpect(status().is4xxClientError());
	}

	@Test
	void testContiNoAuth() throws Exception {
		ResultActions result = mockMvc.perform(get("/api/conti"));
		result.andExpect(status().is4xxClientError());
	}

	// ############## INVALID JWT ##############
	@Test
	void testClientiInvalidJWT() throws Exception {
		Cookie cookie = new Cookie("bearer", "paoskdpaoskdpoaksdpokaspdoksapodkpaoksdpoaksdpoka");
		ResultActions result = mockMvc.perform(get("/api/clienti").cookie(cookie));
		result.andExpect(status().is4xxClientError());
	}
}
