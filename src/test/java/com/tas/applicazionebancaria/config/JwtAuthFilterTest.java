package com.tas.applicazionebancaria.config;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.tas.applicazionebancaria.businesscomponent.model.Amministratore;
import com.tas.applicazionebancaria.repository.AmministratoreRepository;
import com.tas.applicazionebancaria.utils.Costanti;
import com.tas.applicazionebancaria.utils.JWT;

import jakarta.servlet.http.Cookie;

@TestInstance(Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@SpringBootTest
class JwtAuthFilterTest implements Costanti {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	AmministratoreRepository repo;
	Amministratore admin;
	@MockBean
	JWT jwt;
	

	@BeforeAll
	void setup() {
		Amministratore a = new Amministratore();
		a.setNomeAdmin("Piero");
		a.setCognomeAdmin("Feltrin");
		a.setEmailAdmin("test.test@gmail.com");
		a.setPasswordAdmin(BCryptEncoder.encode("Password01$"));
		admin = repo.save(a);
	}
	
	@AfterAll
	void tearDown() {
		repo.delete(admin);
	}
	
	@Test
	void testDoFilterInternalNoCookie() throws Exception{
		ResultActions result = mockMvc.perform(get("/api/clienti"));
		result.andExpect(status().is4xxClientError());
	}

	@Test
	void testDoFilterInternalInvalidCookie() throws Exception{
		Cookie cookie = new Cookie("bearer", "oiasjdoisajdoiajoidjasoijdoisajdoij");
		
		ResultActions result = mockMvc.perform(get("/api/clienti").cookie(cookie));
		result.andExpect(status().is4xxClientError());
	}

	@Test
	void testDoFilterInternalInvalidCookieName() throws Exception{
		Cookie cookie = new Cookie("invalidname", JWT.generate(admin.getNomeAdmin(), admin.getCognomeAdmin(), admin.getEmailAdmin()));
		
		ResultActions result = mockMvc.perform(get("/api/clienti").cookie(cookie));
		result.andExpect(status().is4xxClientError());
	}
	
	@Test
	void testDoFilterInternal() throws Exception{
		Cookie cookie = new Cookie("bearer", JWT.generate(admin.getNomeAdmin(), admin.getCognomeAdmin(), admin.getEmailAdmin()));
		
		ResultActions result = mockMvc.perform(get("/api/clienti").cookie(cookie));
		result.andExpect(status().isOk());
	}
	
	// Testato cambiando l'expiration nella generazione del token a 10s
}
