package com.tas.applicazionebancaria.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

// COMPONENTE RELATIVO AL CONTROLLO DEL BRUTE FORCE....NON ANCORA IMPLEMENTATO DA NESSUNA PARTE !!!!!
@Component
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private LoginAttemptService loginAttemptService;

	/**
	 * Metodo che viene chiamato quando si verifica un evento di fallimento di
	 * autenticazione.
	 * 
	 * Il get "request.getHeader("X-Forwarded-For") ottiene l'header X-Forwarded-For
	 * dalla richiesta. Questo header è comunemente utilizzato per determinare
	 * l'indirizzo IP originale di un client che si trova dietro un proxy. Lo split
	 * utilizza il primo indirizzo IP nell'header X-Forwarded-For, che rappresenta
	 * l'IP originale del client.
	 */
	@Override
	public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent e) {
		final String xfHeader = request.getHeader("X-Forwarded-For");
		if (xfHeader == null || xfHeader.isEmpty() || !xfHeader.contains(request.getRemoteAddr())) {
			loginAttemptService.loginFailed(request.getRemoteAddr());
		} else {
			loginAttemptService.loginFailed(xfHeader.split(",")[0]);
		}
	}
}