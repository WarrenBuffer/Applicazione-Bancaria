package com.tas.applicazionebancaria.restcontroller;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tas.applicazionebancaria.businesscomponent.model.Amministratore;
import com.tas.applicazionebancaria.config.BCryptEncoder;
import com.tas.applicazionebancaria.service.AmministratoreService;
import com.tas.applicazionebancaria.service.LogAccessiService;
import com.tas.applicazionebancaria.utils.AccountBlocker;
import com.tas.applicazionebancaria.utils.LoginRequest;

import jakarta.servlet.http.HttpServletResponse;

@TestInstance(Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@TestPropertySource(
		locations = "classpath:test.properties")
class AuthControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	AmministratoreService as;
	
	@Mock
	private ObjectMapper objectMapper;
	@Mock
	AuthenticationManager authenticationManager;
	@Mock
	AmministratoreService adminService;
	@Mock
	AccountBlocker accountBlocker;
	@Mock
	LogAccessiService logService;
	@Mock
	Amministratore amministratore; 
	
	
	@InjectMocks
	AuthController authController;
	
	private static Amministratore admin;
	private String password = "TestPassword01$";
	@BeforeAll
	void setup() {
		Amministratore a = new Amministratore();
		a.setNomeAdmin("Test");
		a.setCognomeAdmin("Test");
		a.setEmailAdmin("testadmin123456789@testadmin123456789.com");
		a.setPasswordAdmin(BCryptEncoder.encode(password));
		admin = as.saveAmministratore(a);
	}
	
	@AfterAll
	void tearDown() {
		as.deleteAmministratore(admin);
	}
	
	@Test
	void testLogin() throws Exception {

		LoginRequest lr = new LoginRequest();
		lr.setEmail(admin.getEmailAdmin());
		lr.setPassword(password);
		
		ResultActions result = mockMvc.perform(post("/loginAdmin").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsBytes(lr)));
		result.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.code").value(0));
	}

	@Test
	void testLoginBadCredentials() throws Exception {
		
		LoginRequest lr = new LoginRequest();
		lr.setEmail(admin.getEmailAdmin());
		lr.setPassword(password + "l");
		
		ResultActions result = mockMvc.perform(post("/loginAdmin").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsBytes(lr)));
		result.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.code").value(1));
	}

	@Test
	void testLoginLocked() throws Exception {
		
		admin.setAccountBloccato(true);
		
		as.saveAmministratore(admin);
		
		LoginRequest lr = new LoginRequest();
		lr.setEmail(admin.getEmailAdmin());
		lr.setPassword(password);
		
		ResultActions result = mockMvc.perform(post("/loginAdmin").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsBytes(lr)));
		result.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.code").value(1));

		admin.setAccountBloccato(false);
		as.saveAmministratore(admin);
	}
	
	@Test
	void testLogout() throws Exception {
		ResultActions result = mockMvc.perform(get("/logoutAdmin"));
		result.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.code").value(0));
	}
	
	@Test
	void testJsonException() throws Exception {
		
		
		HttpServletResponse response=mock(HttpServletResponse.class);
		
		LoginRequest loginRequest=new LoginRequest();
		loginRequest.setEmail("testadmin123456789@testadmin123456789.com");
		loginRequest.setPassword(password);
		when(authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())))
			.thenThrow(new RuntimeException("JsonProcessingException"));
		
		
		authController.loginAdmin(loginRequest, response);
	}
}