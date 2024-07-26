package com.tas.applicazionebancaria.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.tas.applicazionebancaria.businesscomponent.model.ClienteMongo;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Repository("ClienteMongoRepository")
public interface ClienteMongoRepository extends MongoRepository<ClienteMongo, String>{
	@Query("{ 'emailCliente' : ?0 }")
	Optional<ClienteMongo> findByEmailCliente(String email);
}
