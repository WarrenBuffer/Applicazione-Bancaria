package com.tas.applicazionebancaria.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
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
import com.tas.applicazionebancaria.businesscomponent.model.Cliente;
import com.tas.applicazionebancaria.businesscomponent.model.Conto;
import com.tas.applicazionebancaria.businesscomponent.model.MovimentiConto;
import com.tas.applicazionebancaria.businesscomponent.model.TransazioniBancarie;
import com.tas.applicazionebancaria.service.ClienteService;
import com.tas.applicazionebancaria.service.ContoService;
import com.tas.applicazionebancaria.service.MovimentiContoService;
import com.tas.applicazionebancaria.service.TransazioniBancarieService;
import com.tas.applicazionebancaria.utils.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.http.Cookie;

@SpringBootTest(classes = ApplicazioneBancariaApplication.class)
@AutoConfigureMockMvc
class SelContoTest {
	
	MockedStatic<JWT> mockedStatic;
	
	static String token=JWT.generate("Mario", "Rossi", "mario.rossi@email.it");
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ClienteService clienteService;
	
	@MockBean
	private ContoService contoService;
	
	@MockBean
	private MovimentiContoService movimentiContoService;
	
	@MockBean
	private TransazioniBancarieService transazioniBancarieService;
	
	
	@BeforeEach
	public void setUp() {
		mockedStatic = mockStatic(JWT.class);
	}
	
	
	@Test
	void selcontoTestContoEsistenteOggettiNulli() throws Exception {
		long codConto = 953;
		
		Set<Conto> conti = new HashSet<Conto>();
		conti.add(new Conto());
		
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
	    
	    cliente.setConti(conti);
	    
	    
	    when(movimentiContoService.findUltimi10(codConto)).thenReturn(null);
	    when(transazioniBancarieService.findUltime10(codConto)).thenReturn(null);
	    mockMvc.perform(post("/selconto")
	            .param("codConto", String.valueOf(codConto))
	            .cookie(new Cookie("token", token)))
	        .andExpect(status().isOk())
	        .andExpect(model().attributeExists("nome"))
	        .andExpect(model().attributeExists("cliente"))
	        .andExpect(model().attributeExists("listaConti"))
	        .andExpect(model().attributeExists("conto"))
	        .andExpect(model().attributeDoesNotExist("listaMovimenti"))
	        .andExpect(model().attributeDoesNotExist("listaTransazioni"))
	        .andExpect(view().name("visualizzaconti"));
	}
	
	@Test
	void selcontoTestContoEsistenteOggettiEsistenti() throws Exception {
		long codConto = 953;
		
		Set<Conto> conti = new HashSet<Conto>();
		conti.add(new Conto());
		
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
	  
	    cliente.setConti(conti);
	    
	    List<MovimentiConto> listaMov = Arrays.asList(new MovimentiConto(), new MovimentiConto());
	    when(movimentiContoService.findUltimi10(codConto)).thenReturn(listaMov);
	    
	    List<TransazioniBancarie> listaT = Arrays.asList(new TransazioniBancarie(), new TransazioniBancarie());
	    when(transazioniBancarieService.findUltime10(codConto)).thenReturn(listaT);
	    
	    mockMvc.perform(post("/selconto")
	            .param("codConto", String.valueOf(codConto))
	            .cookie(new Cookie("token", token)))
	        .andExpect(status().isOk())
	        .andExpect(model().attributeExists("nome"))
	        .andExpect(model().attributeExists("cliente"))
	        .andExpect(model().attributeExists("listaConti"))
	        .andExpect(model().attributeExists("conto"))
	        .andExpect(model().attributeExists("listaMovimenti"))
	        .andExpect(model().attributeExists("listaTransazioni"))
	        .andExpect(view().name("visualizzaconti"));
	}
	
	@Test
	void selcontoTestContoEsistenteMovNull() throws Exception {
		long codConto = 953;
		
		Set<Conto> conti = new HashSet<Conto>();
		conti.add(new Conto());
		
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
	    
	    cliente.setConti(conti);
	    
	    
	   
	    when(movimentiContoService.findUltimi10(codConto)).thenReturn(null);
	    List<TransazioniBancarie> listaT = Arrays.asList(new TransazioniBancarie(), new TransazioniBancarie());
	    when(transazioniBancarieService.findUltime10(codConto)).thenReturn(listaT);
	    mockMvc.perform(post("/selconto")
	            .param("codConto", String.valueOf(codConto))
	            .cookie(new Cookie("token", token)))
	        .andExpect(status().isOk())
	        .andExpect(model().attributeExists("nome"))
	        .andExpect(model().attributeExists("cliente"))
	        .andExpect(model().attributeExists("listaConti"))
	        .andExpect(model().attributeExists("conto"))
	        .andExpect(model().attributeDoesNotExist("listaMovimenti"))
	        .andExpect(model().attributeExists("listaTransazioni"))
	        .andExpect(view().name("visualizzaconti"));
	}
	
	@Test
	void selcontoTestContoEsistenteTransazioniNull() throws Exception {
		long codConto = 953;
		
		Set<Conto> conti = new HashSet<Conto>();
		conti.add(new Conto());
		
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
	    
	    cliente.setConti(conti);
	    
	    
	    List<MovimentiConto> listaMov = Arrays.asList(new MovimentiConto(), new MovimentiConto());
	    when(movimentiContoService.findUltimi10(codConto)).thenReturn(listaMov);
	    when(transazioniBancarieService.findUltime10(codConto)).thenReturn(null);
	    mockMvc.perform(post("/selconto")
	            .param("codConto", String.valueOf(codConto))
	            .cookie(new Cookie("token", token)))
	        .andExpect(status().isOk())
	        .andExpect(model().attributeExists("nome"))
	        .andExpect(model().attributeExists("cliente"))
	        .andExpect(model().attributeExists("listaConti"))
	        .andExpect(model().attributeExists("conto"))
	        .andExpect(model().attributeExists("listaMovimenti"))
	        .andExpect(model().attributeDoesNotExist("listaTransazioni"))
	        .andExpect(view().name("visualizzaconti"));
	}
	
	@Test
	void selcontoTestContoEsistenteOggettiVuoti() throws Exception {
		long codConto = 953;
		
		Set<Conto> conti = new HashSet<Conto>();
		conti.add(new Conto());
		
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
	  
	    cliente.setConti(conti);
	    
	    List<MovimentiConto> listaMov = Collections.emptyList();
	    when(movimentiContoService.findUltimi10(codConto)).thenReturn(listaMov);
	    
	    List<TransazioniBancarie> listaT = Collections.emptyList();
	    when(transazioniBancarieService.findUltime10(codConto)).thenReturn(listaT);
	    
	    mockMvc.perform(post("/selconto")
	            .param("codConto", String.valueOf(codConto))
	            .cookie(new Cookie("token", token)))
	        .andExpect(status().isOk())
	        .andExpect(model().attributeExists("nome"))
	        .andExpect(model().attributeExists("cliente"))
	        .andExpect(model().attributeExists("listaConti"))
	        .andExpect(model().attributeExists("conto"))
	        .andExpect(model().attributeDoesNotExist("listaMovimenti"))
	        .andExpect(model().attributeDoesNotExist("listaTransazioni"))
	        .andExpect(view().name("visualizzaconti"));
	}
	
	@Test
	void selcontoTestContIVuoto() throws Exception {
		long codConto = 953;
		
		Set<Conto> conti = new HashSet<Conto>();
		
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
	  
	    cliente.setConti(conti);
	    
	    mockMvc.perform(post("/selconto")
	            .param("codConto", String.valueOf(codConto))
	            .cookie(new Cookie("token", token)))
	        .andExpect(status().isOk())
	        .andExpect(model().attributeExists("nome"))
	        .andExpect(model().attributeExists("cliente"))
	        .andExpect(model().attributeDoesNotExist("listaConti"))
	        .andExpect(model().attributeDoesNotExist("conto"))
	        .andExpect(model().attributeDoesNotExist("listaMovimenti"))
	        .andExpect(model().attributeDoesNotExist("listaTransazioni"))
	        .andExpect(view().name("visualizzaconti"));
	}
	
	@Test
	void selcontoTestContoNonEsiste() throws Exception {
		long codConto = 0;
	    when(contoService.findById(codConto)).thenReturn(Optional.empty());
	   
	    Jws<Claims> mockClaimsJws = mock(Jws.class);
	    Claims mockClaims = mock(Claims.class);
	    when(mockClaimsJws.getBody()).thenReturn(mockClaims);
	    when(mockClaims.getSubject()).thenReturn("mario.rossi@email.it");
	    when(mockClaims.get("nome")).thenReturn("Mario");
	    
	    when(JWT.validate(token)).thenReturn(mockClaimsJws);
	    
	    mockMvc.perform(post("/selconto")
	            .param("codConto", String.valueOf(codConto))
	            .cookie(new Cookie("token", token)))
	        .andExpect(status().isOk())
	        .andExpect(model().attributeDoesNotExist("nome"))
	        .andExpect(model().attributeDoesNotExist("cliente"))
	        .andExpect(model().attributeDoesNotExist("listaConti"))
	        .andExpect(model().attributeDoesNotExist("conto"))
	        .andExpect(model().attributeDoesNotExist("listaMovimenti"))
	        .andExpect(model().attributeDoesNotExist("listaTransazioni"))
	        .andExpect(view().name("visualizzaconti"));
	}
	
	@AfterEach
    public void teardown() {
        mockedStatic.close();
    }

}
