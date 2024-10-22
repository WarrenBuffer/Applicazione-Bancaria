package com.tas.applicazionebancaria.controller;

import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.tas.applicazionebancaria.ApplicazioneBancariaApplication;
import com.tas.applicazionebancaria.service.ClienteService;
import com.tas.applicazionebancaria.utils.JWT;

import jakarta.servlet.http.Cookie;

@SpringBootTest(classes = ApplicazioneBancariaApplication.class)
@AutoConfigureMockMvc
class LogoutClientTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ClienteService clienteService;

	@AfterEach
	void tearDown() throws Exception {
		reset(clienteService);
	}

	/************** POST_LOGOUT_NO_COOKIE **************/
	@Test
	public void logoutNoCookie() throws Exception {
		MockHttpServletRequestBuilder requestBuilder = post("/logout");

		mockMvc.perform(requestBuilder).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/registrazione"));
	}
	
	
	/************** POST_LOGOUT_CON_COOKIE **************/
	@Test
	public void logoutConCookie() throws Exception {
		MockHttpServletRequestBuilder requestBuilder = post("/logout").cookie(new Cookie("token", JWT.generate("test", "testC", "test@test.com")));
		
		mockMvc.perform(requestBuilder).andExpect(status().is3xxRedirection())
		.andExpect(cookie().exists("token"))
		.andExpect(cookie().maxAge("token", 0))
		.andExpect(view().name("redirect:/login"));
	}

}
