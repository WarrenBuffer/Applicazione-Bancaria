package com.tas.applicazionebancaria.service;

import java.util.List;
import java.util.Optional;

import com.tas.applicazionebancaria.businesscomponent.model.MovimentiContoMongo;

public interface MovimentiContoMongoService {
	MovimentiContoMongo saveMovimentiContoMongo(MovimentiContoMongo movimentiContoMongo);
	List<MovimentiContoMongo> findAll();
	Optional<MovimentiContoMongo> findById(String id);
	void deleteMovimentiContoMongo(MovimentiContoMongo movimentiContoMongo);
}
