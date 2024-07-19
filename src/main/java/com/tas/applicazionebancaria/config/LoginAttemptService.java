package com.tas.applicazionebancaria.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tas.applicazionebancaria.repository.AmministratoreRepository;
import com.tas.applicazionebancaria.repository.ClienteRepository;
import com.tas.applicazionebancaria.utils.Costanti;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class LoginAttemptService implements Costanti {

	// private TreeMap<String, Integer> tentativi = new TreeMap<String, Integer>();

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private AmministratoreRepository ar;

	@Autowired
	private ClienteRepository cr;

	public void loginFailed(final String email) {

		/************** URI ADMIN ****************/
		if (request.getRequestURI().equals("/loginAdmin")) {
			// System.out.println("login admin");

			// verifico che non sia già bloccato
			if (ar.getStatoBloccatoByEmail(email) == true) {
				return;
			}
			// se non è bloccato ma ha i tentativi non ancora a 5 esatti li incremento
			if (ar.getStatoBloccatoByEmail(email) == false && ar.getTentativiErratiByEmail(email) <= MAX_TENTATIVI) {
				ar.incrementaTentativoErratoByEmail(email);
			}

			// blocco l'account dopo 5 tentavi
			if (ar.getTentativiErratiByEmail(email) > MAX_TENTATIVI) {
				ar.setBloccatoTrueByEmail(email);
			}
		}

		/************** URI CLIENTE ****************/
		if (request.getRequestURI().equals("/login")) {

			// System.out.println("login client");
			// verifico che non sia già bloccato
			if (cr.getStatoBloccatoByEmail(email) == true) {
				return;
			}
			// se non è bloccato ma ha i tentativi non ancora a 5 esatti li incremento
			if (cr.getStatoBloccatoByEmail(email) == false && cr.getTentativiErratiByEmail(email) <= MAX_TENTATIVI) {
				cr.incrementaTentativoErratoByEmail(email);
			}

			// blocco l'account dopo 5 tentavi
			if (cr.getTentativiErratiByEmail(email) > MAX_TENTATIVI) {
				cr.setBloccatoTrueByEmail(email);
			}
		}
	}

	public boolean isBlocked(final String email) {
		return cr.getStatoBloccatoByEmail(email);
	}

	/****** IMPLEMENTAZIONE CON BLOCCO INDIRIZZO IP *****/
	/*
	 * public boolean isBlocked() { return tentativi.get(getClientIP()) >=
	 * MAX_TENTATIVI; }
	 * 
	 * private String getClientIP() { final String xfHeader =
	 * request.getHeader("X-Forwarded-For"); if (xfHeader != null) { return
	 * xfHeader.split(",")[0]; } return request.getRemoteAddr(); }
	 */
}