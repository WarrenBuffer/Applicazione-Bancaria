package com.tas.applicazionebancaria.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tas.applicazionebancaria.businesscomponent.model.TransazioniMongo;
import com.tas.applicazionebancaria.repository.TransazioniMongoRepository;
import com.tas.applicazionebancaria.service.TransazioniMongoService;

@Service
public class TransazioniMongoServiceImpl implements TransazioniMongoService{
	@Autowired
	TransazioniMongoRepository tmr;

	@Override
	public TransazioniMongo saveTransazioniMongo(TransazioniMongo transazioniMongo) {
		return tmr.save(transazioniMongo);
	}

	@Override
	public List<TransazioniMongo> findAll() {
		return tmr.findAll();
	}

	@Override
	public Optional<TransazioniMongo> findById(String id) {
		return tmr.findById(id);
	}

	@Override
	public void deleteTransazioniMongo(TransazioniMongo transazioniMongo) {
		tmr.delete(transazioniMongo);
	}

	@Override
	public List<TransazioniMongo> findTransazioniPerTipo() {
		return tmr.findTransazioniPerTipo();
	}

	@Override
	public List<TransazioniMongo> transazioniMediePerCliente() {
		return tmr.transazioniMediePerCliente();
	}

	@Override
	public List<TransazioniMongo> importoTransazioniPerMese() {
		return tmr.importoTransazioniPerMese();
	}
	
}