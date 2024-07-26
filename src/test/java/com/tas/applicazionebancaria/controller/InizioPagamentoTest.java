package com.tas.applicazionebancaria.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.Collections;
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
import com.tas.applicazionebancaria.businesscomponent.model.Conto;
import com.tas.applicazionebancaria.businesscomponent.model.enumerations.TipoConto;
import com.tas.applicazionebancaria.service.CarteDiCreditoService;
import com.tas.applicazionebancaria.service.ClienteService;
import com.tas.applicazionebancaria.service.ContoService;
import com.tas.applicazionebancaria.utils.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.http.Cookie;

@SpringBootTest(classes = ApplicazioneBancariaApplication.class)
@AutoConfigureMockMvc
class InizioPagamentoTest {
	
	MockedStatic<JWT> mockedStatic;
	
	static String token=JWT.generate("Mario", "Rossi", "mario.rossi@email.it");
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ClienteService clienteService;
	
	@MockBean
	private ContoService contoService;
	
	@MockBean
	private CarteDiCreditoService carteDiCreditoService;
	
	
	@BeforeEach
	public void setUp() {
		mockedStatic = mockStatic(JWT.class);
	}
	
	
	@Test
	void inizioPagamentoOggettiNulli() throws Exception {
		Set<CarteDiCredito> carte = new HashSet<CarteDiCredito>();
		Set<Conto> conti = new HashSet<Conto>();
		
	    Jws<Claims> mockClaimsJws = mock(Jws.class);
	    Claims mockClaims = mock(Claims.class);
	    when(mockClaimsJws.getBody()).thenReturn(mockClaims);
	    when(mockClaims.getSubject()).thenReturn("mario.rossi@email.it");
	    when(mockClaims.get("nome")).thenReturn("Mario");  
	    
	    
	    when(JWT.validate(token)).thenReturn(mockClaimsJws);
	    
	    Cliente cliente = new Cliente();
	    when(clienteService.findByEmail(mockClaims.getSubject())).thenReturn(Optional.of(cliente));
	   
	    cliente.setConti(conti);
	    cliente.setCarte(carte);
	    
	    mockMvc.perform(get("/iniziopagamento")
	            .cookie(new Cookie("token", token)))
	        .andExpect(status().isOk())
	        .andExpect(model().attributeExists("nome"))
	        .andExpect(view().name("pagamento"));
	}
	
	@Test
	void inizioPagamentoOggettiEsistenti() throws Exception {
		Set<CarteDiCredito> carte = new HashSet<CarteDiCredito>();
		carte.add(new CarteDiCredito());
		Set<Conto> conti = new HashSet<Conto>();
		conti.add(new Conto());
		
	    Jws<Claims> mockClaimsJws = mock(Jws.class);
	    Claims mockClaims = mock(Claims.class);
	    when(mockClaimsJws.getBody()).thenReturn(mockClaims);
	    when(mockClaims.getSubject()).thenReturn("mario.rossi@email.it");
	    when(mockClaims.get("nome")).thenReturn("Mario");  
	    
	    
	    when(JWT.validate(token)).thenReturn(mockClaimsJws);
	    
	    Cliente cliente = new Cliente();
	    when(clienteService.findByEmail(mockClaims.getSubject())).thenReturn(Optional.of(cliente));
	    
	    cliente.setConti(conti);
	    cliente.setCarte(carte);
	    
	    mockMvc.perform(get("/iniziopagamento")
	            .cookie(new Cookie("token", token)))
	        .andExpect(status().isOk())
	        .andExpect(model().attributeExists("nome"))
	        .andExpect(model().attributeExists("listaConti"))
	        .andExpect(model().attributeExists("listaCarte"))
	        .andExpect(view().name("pagamento"));
	}
	
	@Test
	void inizioPagamentoListaContiVuota() throws Exception {
		Set<CarteDiCredito> carte = new HashSet<CarteDiCredito>();
		carte.add(new CarteDiCredito());
		Set<Conto> conti = new HashSet<Conto>();
		
		
	    Jws<Claims> mockClaimsJws = mock(Jws.class);
	    Claims mockClaims = mock(Claims.class);
	    when(mockClaimsJws.getBody()).thenReturn(mockClaims);
	    when(mockClaims.getSubject()).thenReturn("mario.rossi@email.it");
	    when(mockClaims.get("nome")).thenReturn("Mario");  
	    
	    
	    when(JWT.validate(token)).thenReturn(mockClaimsJws);
	    
	    Cliente cliente = new Cliente();
	    when(clienteService.findByEmail(mockClaims.getSubject())).thenReturn(Optional.of(cliente));
	    
	    cliente.setConti(conti);
	    cliente.setCarte(carte);
	    
	    mockMvc.perform(get("/iniziopagamento")
	            .cookie(new Cookie("token", token)))
	        .andExpect(status().isOk())
	        .andExpect(model().attributeExists("nome"))
	        .andExpect(model().attributeDoesNotExist("listaConti"))
	        .andExpect(model().attributeExists("listaCarte"))
	        .andExpect(view().name("pagamento"));
	}
	
	@Test
	void inizioPagamentoListaCarteVuota() throws Exception {
		Set<CarteDiCredito> carte = new HashSet<CarteDiCredito>();
		Set<Conto> conti = new HashSet<Conto>();
		conti.add(new Conto());
		
	    Jws<Claims> mockClaimsJws = mock(Jws.class);
	    Claims mockClaims = mock(Claims.class);
	    when(mockClaimsJws.getBody()).thenReturn(mockClaims);
	    when(mockClaims.getSubject()).thenReturn("mario.rossi@email.it");
	    when(mockClaims.get("nome")).thenReturn("Mario");  
	    
	    
	    when(JWT.validate(token)).thenReturn(mockClaimsJws);
	    
	    Cliente cliente = new Cliente();
	    when(clienteService.findByEmail(mockClaims.getSubject())).thenReturn(Optional.of(cliente));
	    
	    cliente.setConti(conti);
	    cliente.setCarte(carte);
	    
	    mockMvc.perform(get("/iniziopagamento")
	            .cookie(new Cookie("token", token)))
	        .andExpect(status().isOk())
	        .andExpect(model().attributeExists("nome"))
	        .andExpect(model().attributeDoesNotExist("listaCarte"))
	        .andExpect(model().attributeExists("listaConti"))
	        .andExpect(view().name("pagamento"));
	}
	
	@Test
	void inizioPagamentoContiCartePieniRimuoviRisparmio() throws Exception {
		Set<Conto> conti = new HashSet<Conto>();
		Conto con = new Conto();
		con.setTipoConto(TipoConto.RISPARMIO);
	    conti.add(con);
	    Conto conC = new Conto();
		conC.setTipoConto(TipoConto.CORRENTE);
	    conti.add(conC);
	    
	    Set<CarteDiCredito> carte = new HashSet<CarteDiCredito>();
	    carte.add(new CarteDiCredito());
		
	    Jws<Claims> mockClaimsJws = mock(Jws.class);
	    Claims mockClaims = mock(Claims.class);
	    when(mockClaimsJws.getBody()).thenReturn(mockClaims);
	    when(mockClaims.getSubject()).thenReturn("mario.rossi@email.it");
	    when(mockClaims.get("nome")).thenReturn("Mario");  
	    
	    
	    when(JWT.validate(token)).thenReturn(mockClaimsJws);
	    
	    Cliente cliente = new Cliente();
	    when(clienteService.findByEmail(mockClaims.getSubject())).thenReturn(Optional.of(cliente));
	    cliente.setCarte(carte);
	    cliente.setConti(conti);
	    
	    
	    mockMvc.perform(get("/iniziopagamento")
	            .cookie(new Cookie("token", token)))
	        .andExpect(status().isOk())
	        .andExpect(model().attributeExists("nome"))
	        .andExpect(model().attributeExists("listaConti"))
	        .andExpect(model().attributeExists("listaCarte"))
	        .andExpect(view().name("pagamento"));
	}

	@AfterEach
    public void teardown() {
        mockedStatic.close();
    }
}
