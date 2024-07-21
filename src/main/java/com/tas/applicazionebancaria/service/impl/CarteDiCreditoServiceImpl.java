package com.tas.applicazionebancaria.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tas.applicazionebancaria.businesscomponent.model.CarteDiCredito;
import com.tas.applicazionebancaria.repository.CarteDiCreditoRepository;
import com.tas.applicazionebancaria.service.CarteDiCreditoService;

@Service
public class CarteDiCreditoServiceImpl implements CarteDiCreditoService{
	@Autowired
	CarteDiCreditoRepository ccr;
	
	@Override
	public CarteDiCredito saveCarteDiCredito(CarteDiCredito carteDiCredito) {
		return ccr.save(carteDiCredito);
	}

	@Override
	public List<CarteDiCredito> findAll() {
		return ccr.findAll();
	}

	@Override
	public Optional<CarteDiCredito> findById(long id) {
		return ccr.findById(id);
	}

	@Override
	public void deleteCarteDiCredito(CarteDiCredito carteDiCredito) {
		ccr.delete(carteDiCredito);
	}

	@Override
	public long findNumCarteByCodCliente(long id) {
		return ccr.findNumCarteByCodCliente(id);
	}

	@Override
	public List<CarteDiCredito> findByCodCliente(long id) {
		return ccr.findByCodCliente(id);
	}
}
