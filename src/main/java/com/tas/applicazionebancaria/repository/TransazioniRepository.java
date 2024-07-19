package com.tas.applicazionebancaria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tas.applicazionebancaria.businesscomponent.model.Transazioni;

@Repository("TransazioniRepository")
public interface TransazioniRepository extends JpaRepository<Transazioni, Long>{
//	@Query(value = "select count(*) from transazioni where tipo_transazione = 'ACCREDITO'")
//	long findTransazioniAccredito();
//
//	@Query(value = "select count(*) from transazioni where tipo_transazione = 'ADDEBITO'")
//	long findTransazioniAddebito();
//	
//	@Query(value = "select sum(importo) from transazioni group by month(data_transazione)")
//	List<Double> findTotaleByMese();
}
