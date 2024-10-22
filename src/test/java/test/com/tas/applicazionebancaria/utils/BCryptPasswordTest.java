package test.com.tas.applicazionebancaria.utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


class BCryptPasswordTest {
	@Test
    void testCodifica() {
		// $2a$10$Tw7pOZ5AvHVi7Qsz4Gj2vuJ/uGfd5iNFAHPlyuGgPkQLPrIqQm7T.
        String pass = new BCryptPasswordEncoder().encode("Password01$");
        assertNotNull(pass); // Verifica che la password codificata non sia null
        System.out.println(pass);
    }
}
