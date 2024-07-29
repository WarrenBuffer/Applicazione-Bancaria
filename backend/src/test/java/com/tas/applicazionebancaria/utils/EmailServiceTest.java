package com.tas.applicazionebancaria.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailServiceTest {
	@Autowired
	EmailService emailService;
	
	@Test
	void testSendEmail() {
		emailService.sendEmail("test.test@gmail.com", "Oggetto", "Body email");
	}

}
