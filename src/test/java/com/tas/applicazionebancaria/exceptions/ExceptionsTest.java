package com.tas.applicazionebancaria.exceptions;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ExceptionsTest {

	@Test
	void test() {
		AdminTokenException aexc = new AdminTokenException("test");
		assertEquals("test", aexc.getMessage());
		TokenException exc = new TokenException("test");
		assertEquals("test", exc.getMessage());
	}

}
