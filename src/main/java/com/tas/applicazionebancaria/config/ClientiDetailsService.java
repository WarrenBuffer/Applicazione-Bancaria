package com.tas.applicazionebancaria.config;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.tas.applicazionebancaria.businesscomponent.model.Cliente;
import com.tas.applicazionebancaria.repository.ClienteRepository;

public class ClientiDetailsService implements UserDetailsService {

	// Dichiarazione del repository per accedere ai dati degli utenti
    private ClienteRepository repo;

    // Costruttore che inizializza il repository
    public ClientiDetailsService(ClienteRepository repo) {
        this.repo = repo;
    }
    
    // Metodo principale che carica i dettagli dell'utente dato l'email (username)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
        	//System.out.println(email);
            // Trova l'utente nel repository usando l'email
            Cliente cliente = repo.findByEmail(email).get();
            //System.out.println(cliente);
            // Se l'utente esiste, crea un UserDetails object
            if (cliente != null) {
                return User.withUsername(cliente.getEmailCliente())
                    .accountLocked(!cliente.isAccountBloccato()) // Blocca l'account se non è abilitato
                    .password(cliente.getPasswordCliente())
                    .build();
            }
        } catch (Exception exc) {
            exc.printStackTrace(); // Stampa lo stack trace in caso di eccezione
        }

        // Se l'utente non viene trovato, lancia un'eccezione
        throw new UsernameNotFoundException(email);
    }
}
