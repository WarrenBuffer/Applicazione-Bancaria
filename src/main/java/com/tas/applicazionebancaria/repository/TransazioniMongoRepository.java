package com.tas.applicazionebancaria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tas.applicazionebancaria.businesscomponent.model.TransazioniMongo;

@Repository("TransazioniMongoRepository")
public interface TransazioniMongoRepository extends MongoRepository<TransazioniMongo, String>{
	@Query(value = "db.transazioni.aggregate([{$group: {_id: '$tipoTransazione', count: {$sum: 1}}}, {$group: {_id: null, media: {$avg: '$count'}}}])", nativeQuery = true)
	List<TransazioniMongo> findTransazioniPerTipo();
	@Query(value = "db.transazioni.aggregate([{$group: {_id: '$codCliente', media: {$avg: 1}}}])", nativeQuery = true)
	List<TransazioniMongo> transazioniMediePerCliente();
	@Query(value = " db.transazioni.aggregate([{$group: {_id: {$month: '$dataTransazione'}, count: {$sum: 1}}}])", nativeQuery = true)
	List<TransazioniMongo> importoTransazioniPerMese();
}
