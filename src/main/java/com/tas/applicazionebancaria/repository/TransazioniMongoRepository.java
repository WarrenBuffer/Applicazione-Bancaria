package com.tas.applicazionebancaria.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tas.applicazionebancaria.businesscomponent.model.TransazioniMongo;

@Repository("TransazioniMongoRepository")
public interface TransazioniMongoRepository extends MongoRepository<TransazioniMongo, String>{
	@Aggregation(value = {"{$group: {_id: '$tipoTransazione', count: {$sum: 1}}}", "{$group: {_id: null, media: {$avg: '$count'}}}"})
	List<TransazioniMongo> findTransazioniPerTipo();
	@Aggregation(value = "{$group: {_id: '$codCliente', media: {$avg: 1}}}")
	List<TransazioniMongo> transazioniMediePerCliente();
	@Aggregation(value = {"{$group: {_id: {$month: '$dataTransazione'}", "count: {$sum: 1}}}"})
	List<TransazioniMongo> importoTransazioniPerMese();
}
