package com.tas.applicazionebancaria.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
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
class EliminaCartaTest {
	
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
	void eliminaCartaTestCartaEsistente() throws Exception {
	    
	    long codCarta = 453;
	    CarteDiCredito carta = new CarteDiCredito();
	    when(carteDiCreditoService.findById(codCarta)).thenReturn(Optional.of(carta));
	    Mockito.doNothing().when(carteDiCreditoService).deleteCarteDiCredito(carta);
	    mockMvc.perform(post("/eliminacarta")
	    		.param("codCarta",String.valueOf(codCarta))
	            .cookie(new Cookie("token", token)))
	    	.andExpect(status().is3xxRedirection())
	        .andExpect(view().name("redirect:/visualizzacarte"));
	}
	
	@Test
	void eliminaCartaTestCartaNonEsistente() throws Exception {
		long codCarta = 0;
		when(carteDiCreditoService.findById(codCarta)).thenReturn(Optional.empty());
		mockMvc.perform(post("/eliminacarta")
	    		.param("codCarta",String.valueOf(codCarta))
	            .cookie(new Cookie("token", token)))
	    	.andExpect(status().is3xxRedirection())
	        .andExpect(view().name("redirect:/visualizzacarte"));
	}
	
	@AfterEach
    public void teardown() {
        mockedStatic.close();
    }
}