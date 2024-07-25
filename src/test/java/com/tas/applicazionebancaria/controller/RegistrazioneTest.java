package com.tas.applicazionebancaria.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.tas.applicazionebancaria.ApplicazioneBancariaApplication;
import com.tas.applicazionebancaria.businesscomponent.model.CarteDiCredito;
import com.tas.applicazionebancaria.businesscomponent.model.Cliente;
import com.tas.applicazionebancaria.businesscomponent.model.ClienteMongo;
import com.tas.applicazionebancaria.businesscomponent.model.Conto;
import com.tas.applicazionebancaria.businesscomponent.model.MovimentiConto;
import com.tas.applicazionebancaria.businesscomponent.model.Pagamenti;
import com.tas.applicazionebancaria.businesscomponent.model.Prestiti;
import com.tas.applicazionebancaria.businesscomponent.model.RichiestePrestito;
import com.tas.applicazionebancaria.businesscomponent.model.Transazioni;
import com.tas.applicazionebancaria.businesscomponent.model.TransazioniBancarie;
import com.tas.applicazionebancaria.service.ClienteMongoService;
import com.tas.applicazionebancaria.service.ClienteService;
import com.tas.applicazionebancaria.service.ContoService;

import jakarta.servlet.http.Cookie;

@SpringBootTest(classes = ApplicazioneBancariaApplication.class)
@AutoConfigureMockMvc
class RegistrazioneTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ClienteService clienteService;
	
	@MockBean
	private ClienteMongoService clienteMongoService;
	
	@MockBean
	private BCryptPasswordEncoder bCryptEncoder;
	
	@Test
	void registrazioneTestNoToken() throws Exception {
		mockMvc.perform(get("/registrazione"))
				.andExpect(status().isOk())
				.andExpect(view().name("registrazione"))
				.andExpect(model().attributeExists("cliente"))
				.andExpect(model().attribute("cliente", new Cliente()));
	}
	
	@Test
	void registrazioneTestToken() throws Exception {
		mockMvc.perform(get("/registrazione")
			.cookie(new Cookie("token", "test")))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/home"));	
	}
	
	@Test
	void registrazioneTestClienteGiaEsistente() throws Exception {
		Cliente cliente = new Cliente();
		cliente.setEmailCliente("matteo.rossi@email.it");
		
		Cliente clienteCerca = new Cliente();
        clienteCerca.setEmailCliente("matteo.rossi@email.it");
        when(clienteService.findAll()).thenReturn(List.of(clienteCerca));
		
        mockMvc.perform(post("/registrazione")
                .flashAttr("cliente", cliente))
            .andExpect(view().name("registrazione"))
            .andExpect(model().attributeExists("checkUser"))
            .andExpect(model().attribute("checkUser", "Utente già registrato! Effettua il login."));
	}
	
	@Test
	void registrazioneTestInputNonValidi() throws Exception {
		Cliente cliente = new Cliente();
        cliente.setNomeCliente("123AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa");
        cliente.setEmailCliente("emaildiprova@gmail.com");
        cliente.setCognomeCliente("Rossi");
        cliente.setPasswordCliente("Pass01?");

        Cliente clienteCerca = new Cliente();
        clienteCerca.setEmailCliente("emailnonesistente@email.com");
        when(clienteService.findAll()).thenReturn(List.of(clienteCerca));

        mockMvc.perform(post("/registrazione")
                .flashAttr("cliente", cliente))
            .andExpect(view().name("registrazione"))
            .andExpect(model().attributeExists("validInputs"))
            .andExpect(model().attribute("validInputs", "Campi non validi"));
	}
	
	@Test
	void registrazioneTestInputValidi() throws Exception {
		  Cliente cliente = new Cliente();
	      cliente.setNomeCliente("Mario");
	      cliente.setCognomeCliente("Rossi");
	      cliente.setEmailCliente("mario.rossi@email.com");
	      cliente.setPasswordCliente("Pass01?");
	    
	    
	      when(clienteService.findAll()).thenReturn(List.of());

	       
	      when(clienteService.saveCliente(cliente)).thenReturn(cliente);
	      ClienteMongo clienteMongo = new ClienteMongo();  
	      clienteMongo.setNomeCliente(cliente.getNomeCliente());
	      clienteMongo.setCognomeCliente(cliente.getCognomeCliente());
	      clienteMongo.setPasswordCliente(cliente.getPasswordCliente());
	      clienteMongo.setEmailCliente(cliente.getEmailCliente());
	      clienteMongo.setCodCliente(cliente.getCodCliente());
	        
	      when(clienteMongoService.saveClienteMongo(clienteMongo)).thenReturn(new ClienteMongo());

	        
	      when(bCryptEncoder.encode(cliente.getPasswordCliente())).thenReturn("encodedPassword");

	        mockMvc.perform(post("/registrazione")
	                .flashAttr("cliente", cliente))
	            .andExpect(status().is3xxRedirection())
	            .andExpect(view().name("redirect:/login"));
		
	}

}
