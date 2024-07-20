package com.tas.applicazionebancaria.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tas.applicazionebancaria.businesscomponent.model.TransazioniBancarie;

@Repository("TransazioniBancarieRepository")
public interface TransazioniBancarieRepository extends JpaRepository<TransazioniBancarie, Long>{
	@Query(value = "select data_transazione from transazioni_bancarie where data_transazione >= all (select data_transazione from transazioni_bancarie)", nativeQuery = true)
	Date findUltimaTransazione(); 
	
	@Query(value = "select count(*) from transazioni_bancarie", nativeQuery = true)
	Long findNumTransazioni();
	
	@Query(value = "select coalesce(sum(importo),0) from transazioni_bancarie", nativeQuery = true)
	Double findSommaImporti();
}
