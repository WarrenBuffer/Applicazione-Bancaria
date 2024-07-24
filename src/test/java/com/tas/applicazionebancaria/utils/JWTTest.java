package com.tas.applicazionebancaria.utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class JWTTest {
	String token;
	
	@Test
	void testGenerate() {
		assertNotNull(JWT.generate("Test", "Test", "test@test.com"));
	}

	@Test
	void testValidate() {
		JWT.validate(JWT.generate("Test", "Test", "test@test.com"));
	}
}
