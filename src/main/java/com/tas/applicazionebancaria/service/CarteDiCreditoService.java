package com.tas.applicazionebancaria.service;

import java.util.List;
import java.util.Optional;

import com.tas.applicazionebancaria.businesscomponent.model.CarteDiCredito;

public interface CarteDiCreditoService {
	CarteDiCredito saveCarteDiCredito(CarteDiCredito carteDiCredito);
	List<CarteDiCredito> findAll();
	Optional<CarteDiCredito> findById(long id);
	void deleteCarteDiCredito(CarteDiCredito carteDiCredito);
}
