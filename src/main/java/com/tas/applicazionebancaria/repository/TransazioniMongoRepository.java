package com.tas.applicazionebancaria.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tas.applicazionebancaria.businesscomponent.model.TransazioniMongo;

@Repository("TransazioniMongoRepository")
public interface TransazioniMongoRepository extends MongoRepository<TransazioniMongo, String> {
	@Aggregation(value = { "{$group: {_id: '$tipoTransazione', count: {$sum: 1}}}",
			"{$group: {_id: null, media: {$avg: '$count'}}}" })
	List<TransazioniMongo> findTransazioniPerTipo();

	@Aggregation(pipeline = { "{ $group : { _id: '$codCliente', avgTransactions: { $avg: 1 } } }" })
	List<TransazioniMongo> transazioniMediePerCliente();

	@Aggregation(pipeline = {
			"{ $addFields: { adjustedDataTransazione: { $concat: [{ $substrBytes: ['$dataTransazione', 3, 2] }, '/', { $substrBytes: ['$dataTransazione', 0, 2] }, '/', { $substrBytes: ['$dataTransazione', 6, 4] }] } } }",
			"{ $project: { yearMonth: { $dateToString: { format: '%Y-%m', date: { $dateFromString: { dateString: '$adjustedDataTransazione', format: '%m/%d/%Y' } } } }, importo: 1 } }",
			"{ $group : { _id: '$yearMonth', totaleImporto: { $sum: '$importo' } } }" })
	List<TransazioniMongo> importoTransazioniPerMese();
}
