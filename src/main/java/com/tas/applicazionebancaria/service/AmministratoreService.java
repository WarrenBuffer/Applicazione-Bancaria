package com.tas.applicazionebancaria.service;

import java.util.List;
import java.util.Optional;

import com.tas.applicazionebancaria.businesscomponent.model.Amministratore;

public interface AmministratoreService {
	Amministratore saveAmministratore(Amministratore amministratore);
	List<Amministratore> findAll();
	Optional<Amministratore> findById(long id);
	Optional<Amministratore> findByEmail(String email);
	void deleteAmministratore(Amministratore amministratore);
}
