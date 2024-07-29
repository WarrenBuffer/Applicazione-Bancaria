package com.tas.applicazionebancaria.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

class BCryptEncoderTest {

	@Test
	void testEncode() {
		assertNotNull(BCryptEncoder.encode("Password01$"));
	}

	@Test
	void testEncodeNullPass() {
		try {
			BCryptEncoder.encode(null);
			
			fail("Avrebbe dovuto lanciare l'eccezione");
		} catch (IllegalArgumentException exc) {
			
		}
	}
}
