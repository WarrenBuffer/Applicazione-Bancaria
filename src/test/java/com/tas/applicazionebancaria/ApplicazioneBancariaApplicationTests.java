package com.tas.applicazionebancaria;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicazioneBancariaApplicationTests {
	MockedStatic<SpringApplication> mockedStatic;
	
	@InjectMocks
	ServletInitializer initializer;
	@Mock
	SpringApplicationBuilder springapp;
	@Mock
	SpringApplication application;
	
	@Test
	void contextLoads() {
	}
	@Test
	void instance() {
		new ServletInitializer();
	}
	@Test
	void configure() {
		when(springapp.sources(ApplicazioneBancariaApplication.class)).thenReturn(null);
		initializer.configure(springapp);
	}
	@Test
	void testMain() {
		mockedStatic =mockStatic(SpringApplication.class);
		
		when(SpringApplication.run(ApplicazioneBancariaApplication.class, null)).thenReturn(null);
		ApplicazioneBancariaApplication.main(null);
		mockedStatic.close();
	}
}
