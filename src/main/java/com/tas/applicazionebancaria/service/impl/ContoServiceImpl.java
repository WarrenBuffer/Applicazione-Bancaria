package com.tas.applicazionebancaria.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tas.applicazionebancaria.businesscomponent.model.Conto;
import com.tas.applicazionebancaria.repository.ContoRepository;
import com.tas.applicazionebancaria.service.ContoService;

@Service
public class ContoServiceImpl implements ContoService {
	@Autowired
	ContoRepository cr;
	
	@Override
	public Conto saveConto(Conto conto) {
		return cr.save(conto);
	}

	@Override
	public List<Conto> findAll() {
		return cr.findAll();
	}

	@Override
	public Optional<Conto> findById(long id) {
		return cr.findById(id);
	}

	@Override
	public void deleteConto(Conto conto) {
		cr.delete(conto);
	}


	public double findSaldoMedio() {
		return cr.findSaldoMedio();
	}

	@Override
	public long findNumContiByCodCliente(long id) {
		return cr.findNumContiByCodCliente(id);
	}

	@Override
	public List<Conto> findConti0() {
		return cr.findConti0();
	}
	
	@Override
	public List<Conto> findByIdCliente(long id) {
		return cr.findByIdCliente(id);

	}
	
}
