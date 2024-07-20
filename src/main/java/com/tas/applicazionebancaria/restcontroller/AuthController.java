package com.tas.applicazionebancaria.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tas.applicazionebancaria.businesscomponent.model.Amministratore;
import com.tas.applicazionebancaria.service.AmministratoreService;
import com.tas.applicazionebancaria.utils.AccountBlocker;
import com.tas.applicazionebancaria.utils.EmailService;
import com.tas.applicazionebancaria.utils.JWT;
import com.tas.applicazionebancaria.utils.LoginRequest;
import com.tas.applicazionebancaria.utils.ServerResponse;

import jakarta.servlet.http.HttpServletResponse;

@RequestMapping(value = "/")
@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class AuthController {
	@Autowired
	AmministratoreService adminService;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired 
	AccountBlocker accountBlocker;
	@Autowired
	EmailService emailService;

	@PostMapping("/loginAdmin")
	public ServerResponse loginAdmin(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
			Amministratore admin = adminService.findByEmail(loginRequest.getEmail()).get();
			String token = JWT.generate(admin.getNomeAdmin(), admin.getCognomeAdmin(), admin.getEmailAdmin());
			return new ServerResponse(0, token);
		} catch (BadCredentialsException exc) {
			emailService.sendEmail(loginRequest.getEmail(), "Tentativo di login al tuo account.", "Il tuo account potrebbe essere stato compromesso. Se non sei stato tu a fare l'accesso, contatta l'amministratore.");
			accountBlocker.invalidAdmin(loginRequest.getEmail());
			return new ServerResponse(1, "Credenziali non valide");
		} catch (LockedException exc) {
			return new ServerResponse(1, "Account bloccato. Contattare l'amministratore");
		} catch (DisabledException exc) {
			return new ServerResponse(1, "Account disabilitato. Contattare l'amministratore");
		}
	}
}
