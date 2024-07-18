package com.tas.applicazionebancaria.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.tas.applicazionebancaria.businesscomponent.model.ClienteMongo;


@Repository("ClienteMongoRepository")
public interface ClienteMongoRepository extends MongoRepository<ClienteMongo, Long>{
	@Query("{ 'emailCliente' : ?0 }")
	Optional<ClienteMongo> findByEmailCliente(String email);
}
