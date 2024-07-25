package com.tas.applicazionebancaria.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.tas.applicazionebancaria.ApplicazioneBancariaApplication;

import jakarta.servlet.http.Cookie;

@SpringBootTest(classes = ApplicazioneBancariaApplication.class)
@AutoConfigureMockMvc
class HomePageTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	void homePageTestNoToken() throws Exception {
		mockMvc.perform(get("/"))
			.andExpect(status().isOk())
			.andExpect(view().name("homepage"));
	}
	
	@Test
	void homePageTestToke() throws Exception {
		mockMvc.perform(get("/")
			.cookie(new Cookie("token", "test")))
			.andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/home"));	
	}

}
