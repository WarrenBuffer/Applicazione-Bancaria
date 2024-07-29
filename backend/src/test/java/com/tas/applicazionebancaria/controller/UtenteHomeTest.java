package com.tas.applicazionebancaria.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.tas.applicazionebancaria.ApplicazioneBancariaApplication;
import com.tas.applicazionebancaria.utils.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.http.Cookie;

@SpringBootTest(classes = ApplicazioneBancariaApplication.class)
@AutoConfigureMockMvc
class UtenteHomeTest {

	MockedStatic<JWT> mockedStatic;
	
	static String token=JWT.generate("Mario", "Rossi", "mario.rossi@email.it");
	
	@Autowired
	private MockMvc mockMvc;
	
	@BeforeEach
	public void setUp() {
		mockedStatic = mockStatic(JWT.class);
	}
	
	@Test
	void homeTestToken() throws Exception {
        
        Jws<Claims> mockClaims = mock(Jws.class);
        Claims claims = mock(Claims.class);
        
        when(JWT.validate(token)).thenReturn(mockClaims);
        when(mockClaims.getBody()).thenReturn(claims);
        when(claims.get("nome")).thenReturn("Mario Rossi");

        mockMvc.perform(get("/home")
                .cookie(new Cookie("token", token)))
            .andExpect(status().isOk())
            .andExpect(view().name("home"))
            .andExpect(model().attribute("nome", "Mario Rossi"));
    }	
	
	@AfterEach
    public void teardown() {
        mockedStatic.close();
    }
}
