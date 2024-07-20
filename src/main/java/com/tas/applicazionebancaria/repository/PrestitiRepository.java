package com.tas.applicazionebancaria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tas.applicazionebancaria.businesscomponent.model.Prestiti;

@Repository("PrestitiRepository")
public interface PrestitiRepository extends JpaRepository<Prestiti, Long>{
	@Query(value = "select coalesce(sum(importo), 0) from prestiti where cod_cliente = ?1", nativeQuery = true)
	Double findTotPrestitiByCodCliente(long id);	
}
