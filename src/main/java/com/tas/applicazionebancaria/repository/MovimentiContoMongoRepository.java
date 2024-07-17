package com.tas.applicazionebancaria.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tas.applicazionebancaria.businesscomponent.model.MovimentiContoMongo;

@Repository("MovimentiContoRepository")
public interface MovimentiContoMongoRepository extends MongoRepository<MovimentiContoMongo, String> {
	
}
