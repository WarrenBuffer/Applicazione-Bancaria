package com.tas.applicazionebancaria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tas.applicazionebancaria.businesscomponent.model.RichiestePrestito;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Repository("RichiestePrestitoRepository")
public interface RichiestePrestitoRepository extends JpaRepository<RichiestePrestito, Long>{
	@Query(value = "select * from richieste_prestito where cod_cliente = ?1", nativeQuery = true)
	List<RichiestePrestito> findByCodCliente(long codCliente);
}
