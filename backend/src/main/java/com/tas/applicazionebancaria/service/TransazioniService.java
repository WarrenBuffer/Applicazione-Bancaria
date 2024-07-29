package com.tas.applicazionebancaria.service;

import java.util.List;
import java.util.Optional;

import com.tas.applicazionebancaria.businesscomponent.model.Transazioni;

public interface TransazioniService {
	Transazioni saveTransazioni(Transazioni transazioni);
	List<Transazioni> findAll();
	Optional<Transazioni> findById(long id);
	void deleteTransazioni(Transazioni transazioni);
}
