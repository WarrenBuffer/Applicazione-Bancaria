package com.tas.applicazionebancaria.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tas.applicazionebancaria.businesscomponent.model.RichiestePrestito;
import com.tas.applicazionebancaria.repository.RichiestePrestitoRepository;
import com.tas.applicazionebancaria.service.RichiestePrestitoService;

@Service
public class RichiestePrestitoServiceImpl implements RichiestePrestitoService{
	@Autowired
	RichiestePrestitoRepository rpr;
	
	@Override
	public RichiestePrestito saveRichiestePrestito(RichiestePrestito richiestePrestito) {
		return rpr.save(richiestePrestito);
	}

	@Override
	public List<RichiestePrestito> findAll() {
		return rpr.findAll();
	}

	@Override
	public Optional<RichiestePrestito> findById(long id) {
		return rpr.findById(id);
	}

	@Override
	public void deleteRichiestePrestito(RichiestePrestito richiestePrestito) {
		rpr.delete(richiestePrestito);
	}

	@Override
	public List<RichiestePrestito> findByCodCliente(long codCliente) {
		return rpr.findByCodCliente(codCliente);
	}
}
