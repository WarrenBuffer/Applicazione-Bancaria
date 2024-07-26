package com.tas.applicazionebancaria.controller;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.tas.applicazionebancaria.ApplicazioneBancariaApplication;
import com.tas.applicazionebancaria.businesscomponent.model.Cliente;
import com.tas.applicazionebancaria.service.ClienteService;
import com.tas.applicazionebancaria.utils.AccountBlocker;

import jakarta.servlet.http.Cookie;

@SpringBootTest(classes = ApplicazioneBancariaApplication.class)
@AutoConfigureMockMvc
class LoginClientTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ClienteService clienteService;

	@MockBean
	private AccountBlocker accountBlocker;

	private Cliente cliente;

	@BeforeEach
	void setUp() {
		cliente = new Cliente();
		cliente.setEmailCliente("vazzangabriele4@gmail.com");
		cliente.setPasswordCliente(new BCryptPasswordEncoder().encode("Aags1127!"));
		cliente.setNomeCliente("Gabriele");
		cliente.setCognomeCliente("Vazzana");
	}

	@AfterEach
	public void tearDown() {
		// Resetta i mock tra i test
		reset(clienteService, accountBlocker);
	}

	/************** GET_LOGIN_COOKIE_PRESENTE **************/
	@Test
	public void testLoginWithTokenCookie() throws Exception {
		MockHttpServletRequestBuilder requestBuilder = get("/login").cookie(new Cookie("token", "dummyTokenValue"));

		mockMvc.perform(requestBuilder).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/home"));
	}

	/************** GET_LOGIN_COOKIE_NON_PRESENTE **************/
	@Test
	public void testLoginWithoutTokenCookie() throws Exception {
		// Scenario: Il cookie "token" è assente, ci aspettiamo la vista login
		MockHttpServletRequestBuilder requestBuilder = get("/login");

		mockMvc.perform(requestBuilder).andExpect(status().isOk()) // Lo stato dovrebbe essere OK
				.andExpect(view().name("login")); // Dovrebbe mostrare la vista login
	}

	/************** POST_LOGIN_CORRETTO **************/
	@Test
	public void testLoginPasswordCorretta() throws Exception {

		// Configura il mock del servizio
		when(clienteService.findByEmail("vazzangabriele4@gmail.com")).thenReturn(Optional.of(cliente));
		
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/login")
				.param("email", "vazzangabriele4@gmail.com").param("password", "Aags1127!");

		mockMvc.perform(requestBuilder).andExpect(status().is3xxRedirection()) // Lo stato dovrebbe essere di
																				// redirezione 3xx
				.andExpect(view().name("redirect:/home")) // Controlla l'URL di redirezione
				.andExpect(cookie().exists("token")) // Verifica che il cookie esista
				.andExpect(request().sessionAttribute("email_log", "vazzangabriele4@gmail.com")); // Verifica
																									// l'attributo di
																									// sessione
	}

	/************** POST_LOGIN_PASSWORD_ERRATA **************/
	@Test
	public void testLoginPasswordErrata() throws Exception {

		// Configura il mock del servizio
		when(clienteService.findByEmail("vazzangabriele4@gmail.com")).thenReturn(Optional.of(cliente));

		MockHttpServletRequestBuilder requestBuilder = post("/login").param("email", "vazzangabriele4@gmail.com")
				.param("password", "PasswordErrata");

		mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(view().name("login"))
				.andExpect(model().attributeExists("error")).andExpect(model().attribute("error", "Password errata!"));
	}
	
	
	/************** POST_LOGIN_PASSWORD_CORRETTA_UTENTE_BLOCCATO **************/
	@Test
	public void testLoginPasswordCorrettaUtenteBloccato() throws Exception {
		when(clienteService.findByEmail("vazzangabriele4@gmail.com")).thenReturn(Optional.of(cliente));
	    cliente.setAccountBloccato(true);  
	    cliente.setPasswordCliente(new BCryptPasswordEncoder().encode("Aags1127!"));

	    MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/login")
	            .param("email", "vazzangabriele4@gmail.com")
	            .param("password", "Aags1127!");

	    mockMvc.perform(requestBuilder)
	            .andExpect(status().isOk())  
	            .andExpect(view().name("login")) 
	            .andExpect(model().attributeExists("error"));
																									
	}
	
	/************** POST_LOGIN_PASSWORD_SBAGLIATA_UTENTE_BLOCCATO **************/
	@Test
	public void testLoginPasswordSbagliataUtenteBloccato() throws Exception {
		when(clienteService.findByEmail("vazzangabriele4@gmail.com")).thenReturn(Optional.of(cliente));
	    cliente.setAccountBloccato(true);  
	    cliente.setPasswordCliente(new BCryptPasswordEncoder().encode("testP9?"));

	    MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/login")
	            .param("email", "vazzangabriele4@gmail.com")
	            .param("password", "Aags1127!");

	    mockMvc.perform(requestBuilder)
	            .andExpect(status().isOk())  
	            .andExpect(view().name("login")) 
	            .andExpect(model().attributeExists("error"));
																									
	}
	

	/************** POST_LOGIN_UTENTE_NON_TROVATO **************/
	@Test
	public void testLoginUtenteNonTrovato() throws Exception {
		// Configura il mock del servizio per un utente non trovato
		when(clienteService.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

		MockHttpServletRequestBuilder requestBuilder = post("/login").param("email", "nonexistent@example.com")
				.param("password", "AnyPassword");

		mockMvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(view().name("login"))
				.andExpect(model().attributeExists("error"))
				.andExpect(model().attribute("error", "Utente non trovato!"));
	}
}
