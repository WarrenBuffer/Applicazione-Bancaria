package com.tas.applicazionebancaria.config;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.tas.applicazionebancaria.businesscomponent.model.Amministratore;
import com.tas.applicazionebancaria.repository.AmministratoreRepository;

public class AdminDetailsService implements UserDetailsService {

	// Dichiarazione del repository per accedere ai dati degli utenti
    private AmministratoreRepository repo;

    // Costruttore che inizializza il repository
    public AdminDetailsService(AmministratoreRepository repo) {
        this.repo = repo;
    }
    
    // Metodo principale che carica i dettagli dell'utente dato l'email (username)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
        	//System.out.println(email);
            // Trova l'utente nel repository usando l'email
            Amministratore amministratore = repo.findByEmail(email).get();
            //System.out.println(cliente);
            // Se l'utente esiste, crea un UserDetails object
            if (amministratore != null) {
                return User.withUsername(amministratore.getEmailAdmin())
                    .accountLocked(!amministratore.isAccountBloccato()) // Blocca l'account se non è abilitato
                    .password(amministratore.getPasswordAdmin())
                    .build();
            }
        } catch (Exception exc) {
            exc.printStackTrace(); // Stampa lo stack trace in caso di eccezione
        }

        // Se l'utente non viene trovato, lancia un'eccezione
        throw new UsernameNotFoundException(email);
    }
}
