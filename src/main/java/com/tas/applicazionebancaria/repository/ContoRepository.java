package com.tas.applicazionebancaria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tas.applicazionebancaria.businesscomponent.model.Conto;

@Repository("ContoRepository")
public interface ContoRepository extends JpaRepository<Conto, Long>{
	@Query(value = "select avg(saldo) from conto")
	double findSaldoMedio();
	
	@Query(value = "select count(*) from conto where cod_cliente = ?1")
	long findNumContiByCodCliente(long id);
	
	@Query(value = "select * from conto where saldo <= 0", nativeQuery = true)
	List<Conto> findConti0();
}
