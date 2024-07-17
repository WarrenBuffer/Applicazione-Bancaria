package com.tas.applicazionebancaria.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.tas.applicazionebancaria.businesscomponent.model.Amministratore;
import com.tas.applicazionebancaria.repository.AmministratoreRepository;
import com.tas.applicazionebancaria.service.AmministratoreService;

public class AmministratoreServiceImpl implements AmministratoreService {
	@Autowired
	AmministratoreRepository ar;
	
	@Override
	public Amministratore saveAmministratore(Amministratore amministratore) {
		return ar.save(amministratore);
	}

	@Override
	public List<Amministratore> findAll() {
		return ar.findAll();
	}

	@Override
	public Optional<Amministratore> findById(long id) {
		return ar.findById(id);
	}

	@Override
	public void deleteAmministratore(Amministratore amministratore) {
		ar.delete(amministratore);
	}
}
