package com.tas.applicazionebancaria.service;

import java.util.List;
import java.util.Optional;

import com.tas.applicazionebancaria.businesscomponent.model.TransazioniMongo;

public interface TransazioniMongoService {
	TransazioniMongo saveTransazioniMongo(TransazioniMongo transazioniMongo);
	List<TransazioniMongo> findAll();
	Optional<TransazioniMongo> findById(String id);
	void deleteTransazioniMongo(TransazioniMongo transazioniMongo);
	long findTotAddebiti();
	long findTotAccrediti();
	long transazioniMediePerCliente();
	List<TransazioniMongo> importoTransazioniPerMese();
}
