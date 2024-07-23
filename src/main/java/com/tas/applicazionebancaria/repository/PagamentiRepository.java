package com.tas.applicazionebancaria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tas.applicazionebancaria.businesscomponent.model.Pagamenti;

@Repository("PagamentiRepository")
public interface PagamentiRepository extends JpaRepository<Pagamenti, Long>{
	@Query(value = "select coalesce(sum(importo), 0) from pagamenti where cod_cliente = ?1", nativeQuery = true)
	Double findTotPagamentiByCodCliente(long id);
	
	@Query(value="select * from pagamenti where cod_cliente=?1 limit 10", nativeQuery = true)
	List<Pagamenti> findUltimi10Pagamenti(long codCliente);
}
