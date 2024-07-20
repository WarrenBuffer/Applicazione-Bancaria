package com.tas.applicazionebancaria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tas.applicazionebancaria.businesscomponent.model.CarteDiCredito;

@Repository("CarteDiCreditoRepository")
public interface CarteDiCreditoRepository extends JpaRepository<CarteDiCredito, Long> {
	@Query(value = "select count(*) from carte_di_credito where cod_cliente = ?1", nativeQuery = true)
	Long findNumCarteByCodCliente(long id);
	
}
