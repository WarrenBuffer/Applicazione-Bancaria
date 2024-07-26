package com.tas.applicazionebancaria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tas.applicazionebancaria.businesscomponent.model.TransazioniBancarie;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Repository("TransazioniBancarieRepository")
public interface TransazioniBancarieRepository extends JpaRepository<TransazioniBancarie, Long>{
	
	@Query(value = "select count(*) from transazioni_bancarie", nativeQuery = true)
	Long findNumTransazioni();
	
	@Query(value = "select coalesce(sum(importo),0) from transazioni_bancarie", nativeQuery = true)
	Double findSommaImporti();
	
	@Query(value="select * from transazioni_bancarie where conto_origine=?1 and tipo_transazione='ADDEBITO' limit 10", nativeQuery = true)
	List<TransazioniBancarie> findUltime10(long contoOrigine);
}
