package com.tas.applicazionebancaria.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tas.applicazionebancaria.businesscomponent.model.MovimentiContoMongo;
import com.tas.applicazionebancaria.repository.MovimentiContoMongoRepository;
import com.tas.applicazionebancaria.service.MovimentiContoMongoService;

@Service
public class MovimentiContoMongoServiceImpl implements MovimentiContoMongoService{
	@Autowired
	MovimentiContoMongoRepository mcmr;
	
	@Override
	public MovimentiContoMongo saveMovimentiContoMongo(MovimentiContoMongo movimentiContoMongo) {
		return mcmr.save(movimentiContoMongo);
	}

	@Override
	public List<MovimentiContoMongo> findAll() {
		return mcmr.findAll();
	}

	@Override
	public Optional<MovimentiContoMongo> findById(String id) {
		return mcmr.findById(id);
	}

	@Override
	public void deleteMovimentiContoMongo(MovimentiContoMongo movimentiContoMongo) {
		mcmr.delete(movimentiContoMongo);
	}

}
