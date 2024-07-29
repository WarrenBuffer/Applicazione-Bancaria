package com.tas.applicazionebancaria.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.tas.applicazionebancaria.ApplicazioneBancariaApplication;
import com.tas.applicazionebancaria.businesscomponent.model.CarteDiCredito;
import com.tas.applicazionebancaria.businesscomponent.model.Cliente;
import com.tas.applicazionebancaria.service.CarteDiCreditoService;
import com.tas.applicazionebancaria.service.ClienteService;
import com.tas.applicazionebancaria.utils.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.http.Cookie;

@SpringBootTest(classes = ApplicazioneBancariaApplication.class)
@AutoConfigureMockMvc
class SelCarteTest {

	MockedStatic<JWT> mockedStatic;
	
	static String token=JWT.generate("Mario", "Rossi", "mario.rossi@email.it");
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ClienteService clienteService;
	
	@MockBean
	private CarteDiCreditoService carteDiCreditoService;
	
	@BeforeEach
	public void setUp() {
		mockedStatic = mockStatic(JWT.class);
	}
	
	@Test
	void richiestaCartaTestCartaEsisteECartePresenti() throws Exception {
		long codCarta = 453;
		
		Set<CarteDiCredito> carte = new HashSet<CarteDiCredito>();
		carte.add(new CarteDiCredito());
		
		CarteDiCredito carta = new CarteDiCredito();
		when(carteDiCreditoService.findById(codCarta)).thenReturn(Optional.of(carta));
		
	    Jws<Claims> mockClaimsJws = mock(Jws.class);
	    Claims mockClaims = mock(Claims.class);
	    when(mockClaimsJws.getBody()).thenReturn(mockClaims);
	    when(mockClaims.getSubject()).thenReturn("mario.rossi@email.it");
	    when(mockClaims.get("nome")).thenReturn("Mario");  
	    
	    when(JWT.validate(token)).thenReturn(mockClaimsJws);
	    
	    Cliente cliente = new Cliente();
	    when(clienteService.findByEmail(mockClaims.getSubject())).thenReturn(Optional.of(cliente));
	    
	    cliente.setCarte(carte);
	    
	    mockMvc.perform(post("/selcarte")
	    		.param("codCarta", String.valueOf(codCarta))
	            .cookie(new Cookie("token", token)))
	    	.andExpect(status().isOk())
	    	.andExpect(model().attributeExists("nome"))
	        .andExpect(model().attributeExists("cliente"))
	    	.andExpect(model().attributeExists("listaCarte"))
	    	.andExpect(model().attributeExists("carta"))
	        .andExpect(view().name("visualizzacarte"));
	}
	
	@Test
	void richiestaCartaTestCartaNonEsiste() throws Exception {
		 long codCarta = 453;

		 when(carteDiCreditoService.findById(codCarta)).thenReturn(Optional.empty());

		 Jws<Claims> mockClaimsJws = mock(Jws.class);
		 Claims mockClaims = mock(Claims.class);
		 when(mockClaimsJws.getBody()).thenReturn(mockClaims);
		 when(mockClaims.getSubject()).thenReturn("mario.rossi@email.it");
		 when(mockClaims.get("nome")).thenReturn("Mario");

		 when(JWT.validate(token)).thenReturn(mockClaimsJws);

		 mockMvc.perform(post("/selcarte")
				 .param("codCarta", String.valueOf(codCarta))
				 .cookie(new Cookie("token", token)))
		 .andExpect(status().isOk())
		 .andExpect(model().attributeExists("nome"))
		 .andExpect(model().attributeDoesNotExist("cliente"))
		 .andExpect(model().attributeDoesNotExist("listaCarte"))
		 .andExpect(model().attributeDoesNotExist("carta"))
		 .andExpect(view().name("visualizzacarte"));
	}
	
	@Test
	void richiestaCartaTestCartaEsisteECarteNonPresenti() throws Exception {
		long codCarta = 453;
		
		Set<CarteDiCredito> carte = new HashSet<CarteDiCredito>();
		
		CarteDiCredito carta = new CarteDiCredito();
		when(carteDiCreditoService.findById(codCarta)).thenReturn(Optional.of(carta));
		
	    Jws<Claims> mockClaimsJws = mock(Jws.class);
	    Claims mockClaims = mock(Claims.class);
	    when(mockClaimsJws.getBody()).thenReturn(mockClaims);
	    when(mockClaims.getSubject()).thenReturn("mario.rossi@email.it");
	    when(mockClaims.get("nome")).thenReturn("Mario");  
	    
	    when(JWT.validate(token)).thenReturn(mockClaimsJws);
	    
	    Cliente cliente = new Cliente();
	    when(clienteService.findByEmail(mockClaims.getSubject())).thenReturn(Optional.of(cliente));
	    
	    cliente.setCarte(carte);
	    
	    mockMvc.perform(post("/selcarte")
	    		.param("codCarta", String.valueOf(codCarta))
	            .cookie(new Cookie("token", token)))
	    	.andExpect(status().isOk())
	    	.andExpect(model().attributeExists("nome"))
	        .andExpect(model().attributeExists("cliente"))
	        .andExpect(view().name("visualizzacarte"));
	}
	
	@AfterEach
    public void teardown() {
        mockedStatic.close();
    }

}
