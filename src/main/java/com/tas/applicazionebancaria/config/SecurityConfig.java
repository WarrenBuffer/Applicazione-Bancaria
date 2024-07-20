package com.tas.applicazionebancaria.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.tas.applicazionebancaria.repository.AmministratoreRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired
	private AmministratoreRepository amministratoreRepository;
	@Autowired
	private JwtAuthFilter authFilter;
	
	
	public SecurityConfig( AmministratoreRepository amministratoreRepository) {
		this.amministratoreRepository = amministratoreRepository;
	}

	@Bean
	DaoAuthenticationProvider adminAuthenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(new AdminDetailsService(this.amministratoreRepository));
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http, @Autowired AuthenticationManager authenticationManager)
			throws Exception {
		return http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(req -> req
						.requestMatchers("/api/**").authenticated()
						.anyRequest().permitAll())
				.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
				.logout(logout -> logout.disable()).build();
	}
}
