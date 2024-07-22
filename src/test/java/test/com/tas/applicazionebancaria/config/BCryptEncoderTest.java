package test.com.tas.applicazionebancaria.config;

import org.junit.jupiter.api.Test;

import com.tas.applicazionebancaria.config.BCryptEncoder;

class BCryptEncoderTest {

	@Test
	void test() {
		// $2a$10$JEXnXmxbkp1TRw1MJmVlGOPUPpv4UjPDJ75tD7xQ.uU/M0UiVU5S.
		String pass = BCryptEncoder.encode("Piero01$");
		System.out.println(pass);
		
	}

}
