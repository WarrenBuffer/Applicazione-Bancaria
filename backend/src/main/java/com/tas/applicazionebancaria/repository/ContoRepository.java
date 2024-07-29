package com.tas.applicazionebancaria.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tas.applicazionebancaria.businesscomponent.model.Conto;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Repository("ContoRepository")
public interface ContoRepository extends JpaRepository<Conto, Long>{

	@Query(value = "select avg(saldo) from conto", nativeQuery = true)
	Double findSaldoMedio();
	
	@Query(value = "select count(*) from conto where cod_cliente = ?1", nativeQuery = true)
	Long findNumContiByCodCliente(long id);
	
	@Query(value = "select * from conto where saldo <= 0", nativeQuery = true)
	List<Conto> findConti0();


	@Query(value="select * from conto where cod_cliente = ?1", nativeQuery = true)
	List<Conto> findByIdCliente(long id);
	
}
