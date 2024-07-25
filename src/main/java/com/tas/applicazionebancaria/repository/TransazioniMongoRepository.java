package com.tas.applicazionebancaria.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tas.applicazionebancaria.businesscomponent.model.TransazioniMongo;

@Repository("TransazioniMongoRepository")
public interface TransazioniMongoRepository extends MongoRepository<TransazioniMongo, String> {
	@Aggregation(pipeline = { "{$group: { _id: '$tipoTransazione', count: {$sum: 1} }}",
			"{$match: {_id: 'ADDEBITO'}}" })
	Optional<Long> findTotAddebiti();
	
	@Aggregation(pipeline = { "{$group: { _id: '$tipoTransazione', count: {$sum: 1} }}",
			"{$match: {_id: 'ACCREDITO'}}" })
	Optional<Long> findTotAccrediti();

	@Aggregation(pipeline = { "{ $group : { _id: '$codCliente', avgTransactions: { $avg: 1 } } }" })
	Optional<Long> transazioniMediePerCliente();

	@Aggregation(pipeline = {
	        "{ $group : { _id: {'$month': '$dataTransazione'}, importo: { $sum: '$importo' } } }"
	    })
	Optional<List<TransazioniMongo>> importoTransazioniPerMese();
}
