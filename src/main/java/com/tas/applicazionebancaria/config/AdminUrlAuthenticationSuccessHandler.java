package com.tas.applicazionebancaria.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.tas.applicazionebancaria.businesscomponent.model.Amministratore;
import com.tas.applicazionebancaria.repository.AmministratoreRepository;
import com.tas.applicazionebancaria.utils.JWT;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AdminUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	@Autowired
	private AmministratoreRepository ar;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {
		String email = (String) request.getAttribute("email");
		Amministratore a = ar.findByEmail(email).get();
		String token = JWT.generate(a.getNomeAdmin(), a.getCognomeAdmin(), a.getEmailAdmin());
		Cookie cookie = new Cookie("token", token);
		response.addCookie(cookie);
		HttpSession session = request.getSession();
		session.setAttribute("email_log", a.getEmailAdmin());
	}
}
