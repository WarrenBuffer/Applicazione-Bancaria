package com.tas.applicazionebancaria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tas.applicazionebancaria.businesscomponent.model.CarteDiCredito;

@Repository("CarteDiCreditoRepository")
public interface CarteDiCreditoRepository extends JpaRepository<CarteDiCredito, Long> {
	
}
