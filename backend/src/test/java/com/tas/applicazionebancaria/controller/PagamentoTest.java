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
import com.tas.applicazionebancaria.businesscomponent.model.ClienteMongo;
import com.tas.applicazionebancaria.businesscomponent.model.Conto;
import com.tas.applicazionebancaria.businesscomponent.model.enumerations.MetodoPagamento;
import com.tas.applicazionebancaria.service.CarteDiCreditoService;
import com.tas.applicazionebancaria.service.ClienteMongoService;
import com.tas.applicazionebancaria.service.ClienteService;
import com.tas.applicazionebancaria.service.ContoService;
import com.tas.applicazionebancaria.service.PagamentiService;
import com.tas.applicazionebancaria.service.TransazioniBancarieService;
import com.tas.applicazionebancaria.utils.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.http.Cookie;

@SpringBootTest(classes = ApplicazioneBancariaApplication.class)
@AutoConfigureMockMvc
class PagamentoTest{
	
	MockedStatic<JWT> mockedStatic;
	
	static String token=JWT.generate("Mario", "Rossi", "mario.rossi@email.it");
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ClienteService clienteService;
	
	@MockBean
	private ContoService contoService;
	
	@MockBean
	private ClienteMongoService clienteMongoService;
	
	@MockBean
	private PagamentiService pagamentiService;
	
	@MockBean
	private TransazioniBancarieService transazioniBancarieService;
	
	@BeforeEach
	public void setUp() {
		mockedStatic = mockStatic(JWT.class);
	}
	
	@Test
	void pagamentoTestContoVuoto() throws Exception {
	    
		MetodoPagamento metodo = MetodoPagamento.BONIFICO;
		double importo = 20;
		String codConto = "";
		long contoDest=1054;
		
		Jws<Claims> mockClaimsJws = mock(Jws.class);
	    Claims mockClaims = mock(Claims.class);
	    when(mockClaimsJws.getBody()).thenReturn(mockClaims);
	    when(mockClaims.getSubject()).thenReturn("mario.rossi@email.it");
	    when(mockClaims.get("nome")).thenReturn("Mario");  
	    
	    when(JWT.validate(token)).thenReturn(mockClaimsJws);
		
		mockMvc.perform(post("/pagamento")
				.param("metodoPagamento",String.valueOf(metodo))
				.param("importo", String.valueOf(importo))
				.param("codConto", codConto)
				.param("contoDest", String.valueOf(contoDest))
	            .cookie(new Cookie("token", token)))
	        .andExpect(status().isOk())
	        .andExpect(model().attributeExists("error"))
	        .andExpect(model().attributeExists("nome"))
	        .andExpect(view().name("home"));
	}
	
	@Test
	void pagamentoTestContoEsisteEContoSorg() throws Exception {
	    
		MetodoPagamento metodo = MetodoPagamento.BONIFICO;
		double importo = 20;
		String codConto = "1054";
		long contoDest=1054;
		
		Jws<Claims> mockClaimsJws = mock(Jws.class);
	    Claims mockClaims = mock(Claims.class);
	    when(mockClaimsJws.getBody()).thenReturn(mockClaims);
	    when(mockClaims.getSubject()).thenReturn("mario.rossi@email.it");
	    when(mockClaims.get("nome")).thenReturn("Mario");  
	    
	    when(JWT.validate(token)).thenReturn(mockClaimsJws);
	    
	    Cliente cliente = new Cliente();
	    when(clienteService.findByEmail(mockClaims.getSubject())).thenReturn(Optional.of(cliente));
	    
	    Conto conto = new Conto();
	    conto.setCodConto(contoDest);
	    List<Conto> listaContiSorgente = Arrays.asList(conto);
	    when(contoService.findByIdCliente(cliente.getCodCliente())).thenReturn(listaContiSorgente);
		
		mockMvc.perform(post("/pagamento")
				.param("metodoPagamento",String.valueOf(metodo))
				.param("importo", String.valueOf(importo))
				.param("codConto", codConto)
				.param("contoDest", String.valueOf(contoDest))
	            .cookie(new Cookie("token", token)))
	        .andExpect(status().isOk())
	        .andExpect(model().attributeExists("error"))
	        .andExpect(model().attributeExists("nome"))
	        .andExpect(view().name("home"));
	}
	
	@Test
	void pagamentoTestContiEsistonoEImportoMaggioreSaldo() throws Exception {
	    
		MetodoPagamento metodo = MetodoPagamento.BONIFICO;
		double importo = 500;
		String codConto = "100";
		long contoDest=1054;
		
		Jws<Claims> mockClaimsJws = mock(Jws.class);
	    Claims mockClaims = mock(Claims.class);
	    when(mockClaimsJws.getBody()).thenReturn(mockClaims);
	    when(mockClaims.getSubject()).thenReturn("mario.rossi@email.it");
	    when(mockClaims.get("nome")).thenReturn("Mario");  
	    
	    when(JWT.validate(token)).thenReturn(mockClaimsJws);
	    
	    Cliente cliente = new Cliente();
	    when(clienteService.findByEmail(mockClaims.getSubject())).thenReturn(Optional.of(cliente));
	    
	    List<Conto> listaContiSorgente = Arrays.asList(new Conto(), new Conto());
	    when(contoService.findByIdCliente(cliente.getCodCliente())).thenReturn(listaContiSorgente);
	    
	    Conto conto = new Conto();
	    when(contoService.findById(Long.valueOf(codConto))).thenReturn(Optional.of(conto));
	    conto.setSaldo(100);
	    
	    Conto contoD = new Conto();
	    when(contoService.findById(contoDest)).thenReturn(Optional.of(contoD));
		
		mockMvc.perform(post("/pagamento")
				.param("metodoPagamento",String.valueOf(metodo))
				.param("importo", String.valueOf(importo))
				.param("codConto", codConto)
				.param("contoDest", String.valueOf(contoDest))
	            .cookie(new Cookie("token", token)))
	        .andExpect(status().isOk())
	        .andExpect(model().attributeExists("error"))
	        .andExpect(model().attributeExists("nome"))
	        .andExpect(view().name("home"));
	}
	
	@Test
	void pagamentoTestImportoMinoreSaldoEContoDestNonEsiste() throws Exception {
	    
		MetodoPagamento metodo = MetodoPagamento.BONIFICO;
		double importo = 100;
		String codConto = "100";
		long contoDest=1054;
		
		Jws<Claims> mockClaimsJws = mock(Jws.class);
	    Claims mockClaims = mock(Claims.class);
	    when(mockClaimsJws.getBody()).thenReturn(mockClaims);
	    when(mockClaims.getSubject()).thenReturn("mario.rossi@email.it");
	    when(mockClaims.get("nome")).thenReturn("Mario");  
	    
	    when(JWT.validate(token)).thenReturn(mockClaimsJws);
	    
	    Cliente cliente = new Cliente();
	    when(clienteService.findByEmail(mockClaims.getSubject())).thenReturn(Optional.of(cliente));
	    
	    List<Conto> listaContiSorgente = Arrays.asList(new Conto(), new Conto());
	    when(contoService.findByIdCliente(cliente.getCodCliente())).thenReturn(listaContiSorgente);
	    
	    Conto conto = new Conto();
	    when(contoService.findById(Long.valueOf(codConto))).thenReturn(Optional.of(conto));
	    conto.setSaldo(500);
	    
	    when(contoService.findById(contoDest)).thenReturn(Optional.empty());
		
		mockMvc.perform(post("/pagamento")
				.param("metodoPagamento",String.valueOf(metodo))
				.param("importo", String.valueOf(importo))
				.param("codConto", codConto)
				.param("contoDest", String.valueOf(contoDest))
	            .cookie(new Cookie("token", token)))
	        .andExpect(status().isOk())
	        .andExpect(model().attributeExists("error"))
	        .andExpect(model().attributeExists("nome"))
	        .andExpect(view().name("home"));
	}
	
	@Test
	void pagamentoTestImportoMinoreSaldoEContoSorgNonEsiste() throws Exception {
	    
		MetodoPagamento metodo = MetodoPagamento.BONIFICO;
		double importo = 100;
		String codConto = "100";
		long contoDest=1054;
		
		Jws<Claims> mockClaimsJws = mock(Jws.class);
	    Claims mockClaims = mock(Claims.class);
	    when(mockClaimsJws.getBody()).thenReturn(mockClaims);
	    when(mockClaims.getSubject()).thenReturn("mario.rossi@email.it");
	    when(mockClaims.get("nome")).thenReturn("Mario");  
	    
	    when(JWT.validate(token)).thenReturn(mockClaimsJws);
	    
	    Cliente cliente = new Cliente();
	    when(clienteService.findByEmail(mockClaims.getSubject())).thenReturn(Optional.of(cliente));
	    
	    List<Conto> listaContiSorgente = Arrays.asList(new Conto(), new Conto());
	    when(contoService.findByIdCliente(cliente.getCodCliente())).thenReturn(listaContiSorgente);
	    
	    when(contoService.findById(Long.valueOf(codConto))).thenReturn(Optional.empty());
	    
	    Conto contoD = new Conto();
	    when(contoService.findById(contoDest)).thenReturn(Optional.of(contoD));
		
		mockMvc.perform(post("/pagamento")
				.param("metodoPagamento",String.valueOf(metodo))
				.param("importo", String.valueOf(importo))
				.param("codConto", codConto)
				.param("contoDest", String.valueOf(contoDest))
	            .cookie(new Cookie("token", token)))
	        .andExpect(status().isOk())
	        .andExpect(model().attributeExists("error"))
	        .andExpect(model().attributeExists("nome"))
	        .andExpect(view().name("home"));
	}
	
	@Test
	void pagamentoTestImportoMinoreSaldoEContiEsistenti() throws Exception {
	    
		MetodoPagamento metodo = MetodoPagamento.BONIFICO;
		double importo = 100;
		String codConto = "100";
		long contoDest=1054;
		
		Jws<Claims> mockClaimsJws = mock(Jws.class);
	    Claims mockClaims = mock(Claims.class);
	    when(mockClaimsJws.getBody()).thenReturn(mockClaims);
	    when(mockClaims.getSubject()).thenReturn("mario.rossi@email.it");
	    when(mockClaims.get("nome")).thenReturn("Mario");  
	    
	    when(JWT.validate(token)).thenReturn(mockClaimsJws);
	    
	    Cliente cliente = new Cliente();
	    when(clienteService.findByEmail(mockClaims.getSubject())).thenReturn(Optional.of(cliente));
	    
	    List<Conto> listaContiSorgente = Arrays.asList(new Conto(), new Conto());
	    when(contoService.findByIdCliente(cliente.getCodCliente())).thenReturn(listaContiSorgente);
	    
	    Conto conto = new Conto();
	    when(contoService.findById(Long.valueOf(codConto))).thenReturn(Optional.of(conto));
	    conto.setSaldo(500);
	    
	    Conto contoD = new Conto();
	    when(contoService.findById(contoDest)).thenReturn(Optional.of(contoD));
	    
	    
	    Conto c1m = new Conto();
	    Conto c2m = new Conto();
	    when(contoService.saveConto(conto)).thenReturn(c1m);
	    when(contoService.saveConto(contoD)).thenReturn(c2m);
	    
	    Cliente c = new Cliente();
	    when(clienteService.findById(c2m.getCodCliente())).thenReturn(Optional.of(c));
	    
	    ClienteMongo clienteM = new ClienteMongo();
	    when(clienteMongoService.findByEmail(cliente.getEmailCliente())).thenReturn(Optional.of(clienteM));
	    ClienteMongo clienteMDest = new ClienteMongo();
	    when(clienteMongoService.findByEmail(c.getEmailCliente())).thenReturn(Optional.of(clienteMDest));
	    
		mockMvc.perform(post("/pagamento")
				.param("metodoPagamento",String.valueOf(metodo))
				.param("importo", String.valueOf(importo))
				.param("codConto", codConto)
				.param("contoDest", String.valueOf(contoDest))
	            .cookie(new Cookie("token", token)))
	        .andExpect(status().isOk())
	        .andExpect(model().attributeExists("success"))
	        .andExpect(model().attributeExists("nome"))
	        .andExpect(view().name("home"));
	}
	
	@AfterEach
    public void teardown() {
        mockedStatic.close();
    }
}