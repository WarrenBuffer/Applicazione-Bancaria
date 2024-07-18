package test.com.tas.applicazionebancaria.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.tas.applicazionebancaria.businesscomponent.model.Amministratore;
import com.tas.applicazionebancaria.utils.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

class JWTTest {

	@Test
	void test() {
		Amministratore admin = new Amministratore();
		admin.setNomeAdmin("Giorgio");
		admin.setCognomeAdmin("Verdi");
		admin.setEmailAdmin("giorgio.verdi@gmail.com");
		String token = JWT.generate(admin);
		Jws<Claims> claims = JWT.validate(token);
		assertEquals(claims.getBody().get("nome"), "Giorgio");
		assertEquals(claims.getBody().get("cognome"), "Verdi");
		assertEquals(claims.getBody().getSubject(), "giorgio.verdi@gmail.com");
	}
}
