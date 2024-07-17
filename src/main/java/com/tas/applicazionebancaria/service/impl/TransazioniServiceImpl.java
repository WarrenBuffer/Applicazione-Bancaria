package com.tas.applicazionebancaria.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tas.applicazionebancaria.businesscomponent.model.Transazioni;
import com.tas.applicazionebancaria.repository.TransazioniRepository;
import com.tas.applicazionebancaria.service.TransazioniService;

@Service
public class TransazioniServiceImpl implements TransazioniService {
	@Autowired
	TransazioniRepository tr;

	@Override
	public Transazioni saveTransazioni(Transazioni transazioni) {
		return tr.save(transazioni);
	}

	@Override
	public List<Transazioni> findAll() {
		return tr.findAll();
	}

	@Override
	public Optional<Transazioni> findById(long id) {
		return tr.findById(id);
	}

	@Override
	public void deleteTransazioni(Transazioni transazioni) {
		tr.delete(transazioni);
	}
}
