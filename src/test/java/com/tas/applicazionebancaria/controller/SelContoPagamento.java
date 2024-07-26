package com.tas.applicazionebancaria.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.HashSet;
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
class SelContoPagamento {
	
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
	void selContoPagamentoContoEsisteEContiCartePieni() throws Exception {
		long codConto=453;
		
		Set<Conto> conti = new HashSet<Conto>();
	    conti.add(new Conto());
	    
	    Set<CarteDiCredito> carte = new HashSet<CarteDiCredito>();
	    carte.add(new CarteDiCredito());
		
		Conto conto = new Conto();
		when(contoService.findById(codConto)).thenReturn(Optional.of(conto));
		
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
	    
	    
	    mockMvc.perform(post("/selcontoPagamento")
	    		.param("codConto", String.valueOf(codConto))
	            .cookie(new Cookie("token", token)))
	        .andExpect(status().isOk())
	        .andExpect(model().attributeExists("nome"))
	        .andExpect(model().attributeExists("listaConti"))
	        .andExpect(model().attributeExists("listaCarte"))
	        .andExpect(view().name("pagamento"));
	}
	
	@Test
	void selContoPagamentoContoEsisteENoListaCarte() throws Exception {
		long codConto=453;
		
		Set<Conto> conti = new HashSet<Conto>();
	    conti.add(new Conto());
		
	    Set<CarteDiCredito> carte = new HashSet<CarteDiCredito>();
	    
		Conto conto = new Conto();
		when(contoService.findById(codConto)).thenReturn(Optional.of(conto));
		
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
	    
	    
	    mockMvc.perform(post("/selcontoPagamento")
	    		.param("codConto", String.valueOf(codConto))
	            .cookie(new Cookie("token", token)))
	        .andExpect(status().isOk())
	        .andExpect(model().attributeExists("nome"))
	        .andExpect(model().attributeDoesNotExist("listaCarte"))
	        .andExpect(model().attributeExists("listaConti"))
	        .andExpect(view().name("pagamento"));
	}
	
	@Test
	void selContoPagamentoContoEsisteENoListaConti() throws Exception {
		long codConto=453;
		
		Set<Conto> conti = new HashSet<Conto>();
	
	    Set<CarteDiCredito> carte = new HashSet<CarteDiCredito>();
	    carte.add(new CarteDiCredito());
	    
		Conto conto = new Conto();
		when(contoService.findById(codConto)).thenReturn(Optional.of(conto));
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
	    
	    mockMvc.perform(post("/selcontoPagamento")
	    		.param("codConto", String.valueOf(codConto))
	            .cookie(new Cookie("token", token)))
	        .andExpect(status().isOk())
	        .andExpect(model().attributeExists("nome"))
	        .andExpect(model().attributeDoesNotExist("listaConti"))
	        .andExpect(model().attributeExists("listaCarte"))
	        .andExpect(view().name("pagamento"));
	}
	
	@Test
	void selContoPagamentoContoNonEsiste() throws Exception {
		long codConto=0;
		
		Conto conto = new Conto();
		when(contoService.findById(codConto)).thenReturn(Optional.empty());
		
	    Jws<Claims> mockClaimsJws = mock(Jws.class);
	    Claims mockClaims = mock(Claims.class);
	    when(mockClaimsJws.getBody()).thenReturn(mockClaims);
	    when(mockClaims.getSubject()).thenReturn("mario.rossi@email.it");
	    when(mockClaims.get("nome")).thenReturn("Mario");  
	    
	    
	    when(JWT.validate(token)).thenReturn(mockClaimsJws);
	        
	    mockMvc.perform(post("/selcontoPagamento")
	    		.param("codConto", String.valueOf(codConto))
	            .cookie(new Cookie("token", token)))
	        .andExpect(status().isOk())
	        .andExpect(view().name("pagamento"));
	}
	
	@Test
	void selContoPagamentoContoEsisteENoListaContiListaCarte() throws Exception {
		long codConto=453;
		
		Set<Conto> conti = new HashSet<Conto>();
	
	    Set<CarteDiCredito> carte = new HashSet<CarteDiCredito>();
	    
		Conto conto = new Conto();
		when(contoService.findById(codConto)).thenReturn(Optional.of(conto));
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
	    
	    mockMvc.perform(post("/selcontoPagamento")
	    		.param("codConto", String.valueOf(codConto))
	            .cookie(new Cookie("token", token)))
	        .andExpect(status().isOk())
	        .andExpect(model().attributeExists("nome"))
	        .andExpect(model().attributeDoesNotExist("listaConti"))
	        .andExpect(model().attributeDoesNotExist("listaCarte"))
	        .andExpect(view().name("pagamento"));
	}
	
	@Test
	void selContoPagamentoContoEsisteEContiCartePieniRimuoviRisparmio() throws Exception {
		long codConto=453;
		
		Set<Conto> conti = new HashSet<Conto>();
		Conto con = new Conto();
		con.setTipoConto(TipoConto.RISPARMIO);
	    conti.add(con);
	    Conto conC = new Conto();
		conC.setTipoConto(TipoConto.CORRENTE);
	    conti.add(conC);
	    
	    Set<CarteDiCredito> carte = new HashSet<CarteDiCredito>();
	    carte.add(new CarteDiCredito());
		
		Conto conto = new Conto();
		when(contoService.findById(codConto)).thenReturn(Optional.of(conto));
		
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
	    
	    
	    mockMvc.perform(post("/selcontoPagamento")
	    		.param("codConto", String.valueOf(codConto))
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