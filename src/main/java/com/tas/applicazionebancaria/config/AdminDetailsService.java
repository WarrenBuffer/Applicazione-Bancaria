package com.tas.applicazionebancaria.config;

import java.util.Optional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tas.applicazionebancaria.businesscomponent.model.Amministratore;
import com.tas.applicazionebancaria.repository.AmministratoreRepository;

@Service
public class AdminDetailsService implements UserDetailsService {
    private AmministratoreRepository repo;

    public AdminDetailsService(AmministratoreRepository repo) {
        this.repo = repo;
    }
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Amministratore> a = repo.findByEmail(email);
        if (a.isPresent()) {
            return User.withUsername(a.get().getEmailAdmin())
                .password(a.get().getPasswordAdmin())
                .accountLocked(a.get().isAccountBloccato())
                .build();
        }
        
        throw new UsernameNotFoundException(email);
    }
}
