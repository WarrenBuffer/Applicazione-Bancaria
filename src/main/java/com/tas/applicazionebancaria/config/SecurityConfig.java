package com.tas.applicazionebancaria.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.tas.applicazionebancaria.repository.AmministratoreRepository;
import com.tas.applicazionebancaria.repository.ClienteRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private ClienteRepository clienteRepository;
	private AmministratoreRepository amministratoreRepository;

	public SecurityConfig(ClienteRepository clienteRepository) {
		this.clienteRepository = clienteRepository;
	}

	@Bean
	DaoAuthenticationProvider clientAuthenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		// Configura il provider con il servizio di dettagli cliente personalizzato
		authProvider.setUserDetailsService(new ClientiDetailsService(this.clienteRepository));
		// Configura il provider con il codificatore di password
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
	
	@Bean
	DaoAuthenticationProvider adminAuthenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		// Configura il provider con il servizio di dettagli cliente personalizzato
		authProvider.setUserDetailsService(new AdminDetailsService(this.amministratoreRepository));
		// Configura il provider con il codificatore di password
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				// Configura le autorizzazioni delle richieste
				.authorizeHttpRequests(
						auth -> auth.requestMatchers("/admin/**").authenticated())

				.formLogin(form -> form.loginPage("/loginAdmin").permitAll())

				.logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logoutAdmin"))
						.logoutSuccessUrl("/admin/"))
				// Configura l'autenticazione di base HTTP
				.httpBasic(withDefaults());

		// Costruisce e ritorna l'oggetto HttpSecurity configurato
		return http.build();
	}

}
