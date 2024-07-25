package com.tas.applicazionebancaria.config;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tas.applicazionebancaria.utils.Costanti;
import com.tas.applicazionebancaria.utils.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter implements Costanti {
	@Autowired
	AdminDetailsService userDetailsService;

	private Cookie getCookieByName(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) return null;
		for (Cookie c : cookies) {
			if (c.getName().equals(cookieName))
				return c;
		}
		return null;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		Cookie token = getCookieByName(request, COOKIE_NAME);
		if (token != null) {
			try {
				Jws<Claims> claims = JWT.validate(token.getValue());
				UserDetails details = userDetailsService.loadUserByUsername(claims.getBody().getSubject());
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
				filterChain.doFilter(request, response);
			} catch (ExpiredJwtException exc) {
				Cookie admin = getCookieByName(request, "admin");
				byte[] json = Base64.getDecoder().decode(admin.getValue());
				
				ObjectMapper mapper = new ObjectMapper();
				JsonNode rootNode = mapper.readTree(json);
				String email = rootNode.get("emailAdmin").asText();
				String nome = rootNode.get("nomeAdmin").asText();
				String cognome = rootNode.get("cognomeAdmin").asText();
				
				UserDetails details = userDetailsService.loadUserByUsername(email);
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
				String newToken = JWT.generate(nome, cognome, email);
				Cookie cookie = new Cookie(COOKIE_NAME, newToken);
				cookie.setMaxAge(COOKIE_MAX_AGE);
				cookie.setPath("/");
				response.addCookie(cookie);
				filterChain.doFilter(request, response);
			} catch (Exception exc) {
				filterChain.doFilter(request, response);
			} 
		} else {
			filterChain.doFilter(request, response);
		}
	}

}
