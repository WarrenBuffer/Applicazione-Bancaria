package test.com.tas.applicazionebancaria.utils;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
class AdminLoginTest {
	@Autowired
	MockMvc mvc;

	@WithUserDetails(value="gab@gmail.com", userDetailsServiceBeanName="AdminDetailsService")
	@Test
	void testCodifica() throws Exception {
		//String pass = new BCryptPasswordEncoder().encode("Pass01$");
		//assertNotNull(pass); // Verifica che la password codificata non sia null
		//System.out.println(pass);
		mvc.perform(MockMvcRequestBuilders.post("/loginAdmin").accept(MediaType.ALL)).andExpect(status().isForbidden());
	}
}
