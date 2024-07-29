package com.tas.applicazionebancaria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tas.applicazionebancaria.businesscomponent.model.CarteDiCredito;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Repository("CarteDiCreditoRepository")
public interface CarteDiCreditoRepository extends JpaRepository<CarteDiCredito, Long> {
	@Query(value = "select count(*) from carte_di_credito where cod_cliente = ?1", nativeQuery = true)
	Long findNumCarteByCodCliente(long id);

	@Query(value = "select * from carte_di_credito where cod_cliente = ?1", nativeQuery = true)
	List<CarteDiCredito> findByCodCliente(long id);

}
