package com.tas.applicazionebancaria.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tas.applicazionebancaria.businesscomponent.model.TransazioniMongo;

@Repository("TransazioniMongoRepository")
public interface TransazioniMongoRepository extends MongoRepository<TransazioniMongo, String>{
	
}
