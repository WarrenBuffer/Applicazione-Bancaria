package com.tas.applicazionebancaria.config;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Base64;
import java.util.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tas.applicazionebancaria.businesscomponent.model.Amministratore;
import com.tas.applicazionebancaria.repository.AmministratoreRepository;
import com.tas.applicazionebancaria.utils.Costanti;
import com.tas.applicazionebancaria.utils.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@TestInstance(Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
class JwtAuthFilterTest implements Costanti {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	AmministratoreRepository repo;
	Amministratore admin;
	
	@MockBean
	JWT jwt;
	@MockBean
	AdminDetailsService userDetailsService;
	
	
	@InjectMocks
    private JwtAuthFilter jwtAuthFilter;
	static String token=JWT.generate("Mario", "Rossi", "mario.rossi@email.it");

	@BeforeAll
	void setup() {
		Amministratore a = new Amministratore();
		a.setNomeAdmin("Piero");
		a.setCognomeAdmin("Feltrin");
		a.setEmailAdmin("test.test@gmail.com");
		a.setPasswordAdmin(BCryptEncoder.encode("Password01$"));
		admin = repo.save(a);
	}
	
	@AfterAll
	void tearDown() {
		repo.delete(admin);
	}
	
	@Order(1)
	@Test
	void testDoFilterInternalNoCookie() throws Exception{
		ResultActions result = mockMvc.perform(get("/api/clienti"));
		result.andExpect(status().is4xxClientError());
	}
	@Order(2)
	@Test
	void testDoFilterInternalInvalidCookie() throws Exception{
		Cookie cookie = new Cookie("bearer", "oiasjdoisajdoiajoidjasoijdoisajdoij");
		
		ResultActions result = mockMvc.perform(get("/api/clienti").cookie(cookie));
		result.andExpect(status().is4xxClientError());
	}
	@Order(3)
	@Test
	void testDoFilterInternalInvalidCookieName() throws Exception{
		Cookie cookie = new Cookie("invalidname", JWT.generate(admin.getNomeAdmin(), admin.getCognomeAdmin(), admin.getEmailAdmin()));
		
		ResultActions result = mockMvc.perform(get("/api/clienti").cookie(cookie));
		result.andExpect(status().is4xxClientError());
	}
	@Order(4)
	@Test
	void testDoFilterInternal() throws Exception{
//		Cookie cookie = new Cookie("bearer", JWT.generate(admin.getNomeAdmin(), admin.getCognomeAdmin(), admin.getEmailAdmin()));
//		
//		ResultActions result = mockMvc.perform(get("/api/clienti").cookie(cookie));
//		result.andExpect(status().isOk());
		
		Amministratore a = new Amministratore();
		a.setNomeAdmin("Piero");
		a.setCognomeAdmin("Feltrin");
		a.setEmailAdmin("test.test@gmail.com");
		a.setPasswordAdmin(BCryptEncoder.encode("Password01$"));
		
		HttpServletRequest request = mock(HttpServletRequest.class);
		Cookie[] cookies=new Cookie[1];
		cookies[0]=new Cookie("bearer", JWT.generate(admin.getNomeAdmin(), admin.getCognomeAdmin(), admin.getEmailAdmin()));
		when(request.getCookies()).thenReturn(cookies);
		
		HttpServletResponse response=mock(HttpServletResponse.class);
		
		FilterChain filterChain=mock(FilterChain.class);
		doNothing().when(filterChain).doFilter(request, response);
		
	    when(userDetailsService.loadUserByUsername("test.test@gmail.com")).thenReturn(
	    		User.withUsername(a.getEmailAdmin())
                .password(a.getPasswordAdmin())
                .accountLocked(a.isAccountBloccato())
                .build());
		
		jwtAuthFilter.doFilterInternal(request, response, filterChain);
		
	}
	@Order(5)
	@Test
	void testDoFilterExpiredCookie() throws Exception {
		
		Amministratore a = new Amministratore();
		a.setNomeAdmin("Piero");
		a.setCognomeAdmin("Feltrin");
		a.setEmailAdmin("test.test@gmail.com");
		a.setPasswordAdmin(BCryptEncoder.encode("Password01$"));
		String json=new ObjectMapper().writeValueAsString(a);

		HttpServletRequest request = mock(HttpServletRequest.class);
		Cookie[] cookies=new Cookie[2];
		cookies[0]=new Cookie("bearer", token);
		cookies[1]=new Cookie("admin", 
				Base64.getEncoder().encodeToString(json.getBytes()));
		when(request.getCookies()).thenReturn(cookies);
		
		HttpServletResponse response=mock(HttpServletResponse.class);
		
		FilterChain filterChain=mock(FilterChain.class);
		doNothing().when(filterChain).doFilter(request, response);
		
		Jws<Claims> mockClaimsJws = mock(Jws.class);
	    Claims mockClaims = mock(Claims.class);
	    when(mockClaimsJws.getBody()).thenReturn(mockClaims);
	    when(mockClaims.getSubject()).thenReturn("test.test@gmail.com");
	    when(mockClaims.getIssuedAt()).thenReturn(new Date());
	    
	    MockedStatic<JWT> mockedStaticJWT=mockStatic(JWT.class);
	    when(JWT.validate(token)).thenThrow(ExpiredJwtException.class);
		
	    when(userDetailsService.loadUserByUsername("test.test@gmail.com")).thenReturn(
	    		User.withUsername(a.getEmailAdmin())
                .password(a.getPasswordAdmin())
                .accountLocked(a.isAccountBloccato())
                .build());
	    
		jwtAuthFilter.doFilterInternal(request, response, filterChain);
	    mockedStaticJWT.close();
		
	}
	
	@Test
	void istanze() {
		JWT jw=new JWT();
		BCryptEncoder bCryptEncoder=new BCryptEncoder();
		System.out.println(jw.toString()+bCryptEncoder.toString());
	}
	// Testato cambiando l'expiration nella generazione del token a 10s
}
