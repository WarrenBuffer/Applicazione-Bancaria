package com.tas.applicazionebancaria.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tas.applicazionebancaria.businesscomponent.model.ClienteMongo;

@Repository("ClienteMongoRepository")
public interface ClienteMongoRepository extends MongoRepository<ClienteMongo, Long>{
	
}
