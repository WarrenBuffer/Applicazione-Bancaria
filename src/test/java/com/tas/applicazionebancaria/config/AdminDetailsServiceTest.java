package com.tas.applicazionebancaria.config;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.tas.applicazionebancaria.businesscomponent.model.Amministratore;
import com.tas.applicazionebancaria.repository.AmministratoreRepository;

@TestInstance(Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
class AdminDetailsServiceTest {
	@Autowired
	AdminDetailsService ads;
	@Autowired
	AmministratoreRepository repo;
	Amministratore admin;
	
	@BeforeAll
	void setup() {
		Amministratore a = new Amministratore();
		a.setNomeAdmin("Test");
		a.setCognomeAdmin("Test");
		a.setEmailAdmin("testadmin123456789@testadmin123456789.com");
		a.setPasswordAdmin(BCryptEncoder.encode("TestPassword01$"));
		admin = repo.save(a);
	}
	
	@AfterAll
	void tearDown() {
		repo.delete(admin);
	}
	
	@Test
	void testLoadUserByUsername() {
		UserDetails details = new AdminDetailsService(repo).loadUserByUsername(admin.getEmailAdmin());
	}
	
	@Test
	void testLoadUserByUsernameNotFound() {
		try {
			new AdminDetailsService(repo).loadUserByUsername("asoudjosaijdoija");
			fail("Avrebbe dovuto lanciare l'eccezione");
		}catch (UsernameNotFoundException exc) {
			
		}
	}
}
