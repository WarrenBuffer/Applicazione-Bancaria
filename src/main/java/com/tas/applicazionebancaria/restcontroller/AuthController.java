package com.tas.applicazionebancaria.restcontroller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tas.applicazionebancaria.businesscomponent.model.Amministratore;
import com.tas.applicazionebancaria.businesscomponent.model.LogAccessiAdmin;
import com.tas.applicazionebancaria.service.AmministratoreService;
import com.tas.applicazionebancaria.service.LogAccessiService;
import com.tas.applicazionebancaria.utils.AccountBlocker;
import com.tas.applicazionebancaria.utils.Costanti;
import com.tas.applicazionebancaria.utils.EmailService;
import com.tas.applicazionebancaria.utils.JWT;
import com.tas.applicazionebancaria.utils.LoginRequest;
import com.tas.applicazionebancaria.utils.ServerResponse;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RequestMapping(value = "/")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
public class AuthController implements Costanti{
	@Autowired
	AmministratoreService adminService;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired 
	AccountBlocker accountBlocker;
	@Autowired
	EmailService emailService;
	@Autowired
	LogAccessiService logService;

	@PostMapping("/loginAdmin")
	public ServerResponse loginAdmin(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
			Amministratore admin = adminService.findByEmail(loginRequest.getEmail()).get();
			accountBlocker.validAdmin(admin.getEmailAdmin());
			String token = JWT.generate(admin.getNomeAdmin(), admin.getCognomeAdmin(), admin.getEmailAdmin());
			Cookie cToken = new Cookie(COOKIE_NAME, token);
			cToken.setMaxAge(COOKIE_MAX_AGE);
			// cToken.setSecure(true); // TODO: Da abilitare quando implementiamo HTTPS
			response.addCookie(cToken);
			Cookie cAdmin = new Cookie("admin", admin.getNomeAdmin());
			cAdmin.setMaxAge(COOKIE_MAX_AGE);
			// cAdmin.setSecure(true); // TODO: Da abilitare quando implementiamo HTTPS
			response.addCookie(cToken);
			response.addCookie(cAdmin);
			
			//logs
			LogAccessiAdmin log = new LogAccessiAdmin();
			log.setCodAdmin(String.valueOf(admin.getCodAdmin()));
			log.setData(new Date());
			log.setDettagli("Login admin id: "+ admin.getCodAdmin());
			logService.saveLogAccesso(log);
			
			return new ServerResponse(0, "Logged in successfully.");
		} catch (BadCredentialsException exc) {
			emailService.sendEmail(loginRequest.getEmail(), "Tentativo di login al tuo account.", "Il tuo account potrebbe essere stato compromesso. Se non sei stato tu a fare l'accesso, contatta l'amministratore.");
			accountBlocker.invalidAdmin(loginRequest.getEmail());
			System.out.println("Inside bad credentials");
			//logs
			Amministratore admin = adminService.findByEmail(loginRequest.getEmail()).get();
			LogAccessiAdmin log = new LogAccessiAdmin();
			log.setCodAdmin(String.valueOf(admin.getCodAdmin()));
			log.setData(new Date());
			log.setDettagli("Tentativo di accesso errato admin id: "+ admin.getCodAdmin());
			logService.saveLogAccesso(log);
			
			return new ServerResponse(1, "Credenziali non valide");
		} catch (LockedException exc) {
			//logs
			Amministratore admin = adminService.findByEmail(loginRequest.getEmail()).get();
			LogAccessiAdmin log = new LogAccessiAdmin();
			log.setCodAdmin(String.valueOf(admin.getCodAdmin()));
			log.setData(new Date());
			log.setDettagli("Login fallito con blocco account admin id: "+ admin.getCodAdmin());
			logService.saveLogAccesso(log);
			
			return new ServerResponse(1, "Account bloccato. Contattare l'amministratore");
		}
	}
	
	@GetMapping("/logoutAdmin") 
	public ServerResponse logoutAdmin(HttpServletResponse response, @CookieValue(name = "bearer", required = false) String token) {

		Cookie cToken = new Cookie(COOKIE_NAME, "");
		Cookie cAdmin = new Cookie("admin", "");
		cToken.setMaxAge(0);
		cAdmin.setMaxAge(0);
		// cToken.setSecure(true); // TODO: Da abilitare quando implementiamo HTTPS
		// cAdmin.setSecure(true); // TODO: Da abilitare quando implementiamo HTTPS
		response.addCookie(cToken);
		response.addCookie(cAdmin);
		
		return new ServerResponse(0, "Logged out successfully.");
	}
}
